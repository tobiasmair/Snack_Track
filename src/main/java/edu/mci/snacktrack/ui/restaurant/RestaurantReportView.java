package edu.mci.snacktrack.ui.restaurant;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
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

    private RestaurantService restaurantService;
    private Long restaurantId;

    private ComboBox<String> filterCombo;
    private Chart salesChart;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String role = (String) VaadinSession.getCurrent().getAttribute("userRole");
        if (!"restaurant".equals(role)) {
            event.forwardTo("");
        }

        Restaurant restaurant = (Restaurant) VaadinSession.getCurrent().getAttribute("user");
        restaurantId = restaurant.getRestaurantId();

        updateChart();
    }

    public RestaurantReportView(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(new H2("Restaurant Sales Report"));

        filterCombo = new ComboBox<>("Filter by");
        filterCombo.setItems("Last Week", "Last Month", "All Time");
        filterCombo.setValue("All Time");

        salesChart = new Chart(ChartType.COLUMN);
        salesChart.setWidth("80%");
        salesChart.setHeight("400px");

        add(filterCombo, salesChart);

        filterCombo.addValueChangeListener(event -> updateChart());
    }

    private void updateChart() {
        if (restaurantId == null) {
            return;
        }

        LocalDate now = LocalDate.now();
        LocalDate fromDate;
        LocalDate toDate = now;

        switch (filterCombo.getValue()) {
            case "Last Week":
                fromDate = now.minusWeeks(1);
                break;
            case "Last Year":
                fromDate = now.minusYears(1).with(TemporalAdjusters.firstDayOfYear());
                toDate = now.with(TemporalAdjusters.lastDayOfYear());
                break;
            case "All Time":
            default:
                fromDate = LocalDate.of(2000, 1, 1); // very far date
                break;
        }

        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = toDate.atTime(23, 59, 59, 999_999_999);

        // Get Map from Service
        Map<String, Number> stats = restaurantService.getSalesStats(restaurantId, fromDateTime, toDateTime);

        Number totalSales = stats.getOrDefault("totalSales", 0);
        Number orderCount = stats.getOrDefault("orderCount", 0);

        // configure Chart
        var chart = salesChart.getConfiguration();
        chart.setTitle("Sales and Orders");

        DataSeries series = new DataSeries();
        series.add(new DataSeriesItem("Sales (EUR)", totalSales.doubleValue()));
        series.add(new DataSeriesItem("Number of orders", orderCount.intValue()));

        chart.setSeries(series);

        salesChart.drawChart();
    }
}
