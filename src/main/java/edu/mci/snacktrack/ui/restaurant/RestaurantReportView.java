package edu.mci.snacktrack.ui.restaurant;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import edu.mci.snacktrack.model.Restaurant;
import edu.mci.snacktrack.service.implementation.RestaurantService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;

@Route(value = "restaurant-report", layout = RestaurantLayout.class)
@PageTitle("Restaurant Report")
public class RestaurantReportView extends VerticalLayout implements BeforeEnterObserver {

    private final RestaurantService restaurantService;
    private Long restaurantId;

    private final ComboBox<String> filterCombo = new ComboBox<>("Time Period");
    private final Span orderCountLabel = new Span();
    private final Span salesSumLabel = new Span();
    private final Chart pieChartDish = new Chart(ChartType.PIE);
    private final Chart pieChartCustomer = new Chart(ChartType.PIE);

    private final HorizontalLayout chartsLayout = new HorizontalLayout();

    public RestaurantReportView(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;

        setupLayout();
        setupFilter();
        setupCharts();

        filterCombo.addValueChangeListener(event -> updateCharts());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String role = (String) VaadinSession.getCurrent().getAttribute("userRole");
        if (!"restaurant".equals(role)) {
            event.forwardTo("");
            return;
        }

        Restaurant restaurant = (Restaurant) VaadinSession.getCurrent().getAttribute("user");
        restaurantId = restaurant.getRestaurantId();

        updateCharts();
    }

    // Layout Setup
    private void setupLayout() {
        setSizeFull();
        setSpacing(true);
        setPadding(true);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        chartsLayout.setWidthFull();
        chartsLayout.setSpacing(true);
        chartsLayout.setAlignItems(Alignment.CENTER);
        chartsLayout.add(pieChartDish, pieChartCustomer);

        add(new H2("Restaurant Sales Report"));
        add(filterCombo, orderCountLabel, salesSumLabel, chartsLayout);
    }

    // Dropdown für Zeitraum
    private void setupFilter() {
        filterCombo.setItems("Last Week", "Last Month", "All Time");
        filterCombo.setValue("All Time");
    }

    // Chart-Einstellungen
    private void setupCharts() {

        pieChartDish.setWidth("45%");
        pieChartDish.setHeight("400px");

        pieChartCustomer.setWidth("45%");
        pieChartCustomer.setHeight("400px");
    }

    // Diagramme aktualisieren
    private void updateCharts() {
        if (restaurantId == null) return;

        // Zeitbereich bestimmen
        LocalDate now = LocalDate.now();
        LocalDate fromDate;
        LocalDate toDate = now;

        switch (filterCombo.getValue()) {
            case "Last Week":
                fromDate = now.minusWeeks(1);
                break;
            case "Last Month":
                fromDate = now.minusMonths(1);
                break;
            case "All Time":
            default:
                fromDate = LocalDate.of(2000, 1, 1); // weit in der Vergangenheit
                break;
        }

        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = toDate.atTime(23, 59, 59, 999_999_999);

        // Statistiken vom Service holen
        Map<String, Number> stats = restaurantService.getSalesStats(restaurantId, fromDateTime, toDateTime);
        Number totalSales = stats.getOrDefault("totalSales", 0);
        Number orderCount = stats.getOrDefault("orderCount", 0);

        orderCountLabel.setText("Number of orders: " + orderCount.intValue());
        salesSumLabel.setText("Sum Sales: " + totalSales.intValue() + "€");

        // Kreisdiagramm nach Gericht
        Map<String, Integer> salesPerDish = restaurantService.getSalesPerDish(restaurantId, fromDateTime, toDateTime);
        Configuration pieConfig = pieChartDish.getConfiguration();
        pieConfig.setTitle("Sales by dish");

        DataSeries pieSeries = new DataSeries();
        salesPerDish.forEach((dish, sum) ->
                pieSeries.add(new DataSeriesItem(dish, sum))
        );

        // Beschriftungen für PieChart
        PlotOptionsPie plotOptionsDish = new PlotOptionsPie();
        DataLabels dataLabelsDish = new DataLabels(true);
        dataLabelsDish.setFormat("{point.name}: {point.y} sales"); // Gericht + Anzahl Bestellungen
        plotOptionsDish.setDataLabels(dataLabelsDish);
        pieSeries.setPlotOptions(plotOptionsDish);

        pieConfig.setSeries(pieSeries);
        pieChartDish.drawChart();


        // Kreisdiagramm Sales pro Kunde
        Map<String, Double> salesPerCustomer = restaurantService.getSalesPerCustomer(restaurantId, fromDateTime, toDateTime);

        // Diagramm-Konfiguration
        Configuration pieConfigCustomer = pieChartCustomer.getConfiguration();
        pieConfigCustomer.setTitle("Sales per Customer");

        DataSeries pieSeriesCustomer = new DataSeries();
        salesPerCustomer.forEach((customerEmail, sum) ->
                pieSeriesCustomer.add(new DataSeriesItem(customerEmail, sum))
        );

        // Beschriftungen für PieChart
        PlotOptionsPie plotOptions = new PlotOptionsPie();
        DataLabels dataLabels = new DataLabels(true);
        dataLabels.setFormat("{point.name}: {point.y:.2f} €"); // Mail + Gesamter Umsatz
        plotOptions.setDataLabels(dataLabels);
        pieSeriesCustomer.setPlotOptions(plotOptions);

        // Setzen und anzeigen
        pieConfigCustomer.setSeries(pieSeriesCustomer);
        pieChartCustomer.drawChart();

    }
}
