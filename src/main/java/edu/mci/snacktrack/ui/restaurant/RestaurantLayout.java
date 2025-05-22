package edu.mci.snacktrack.ui.restaurant;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.theme.lumo.LumoUtility;
import edu.mci.snacktrack.model.Restaurant;

public class RestaurantLayout extends AppLayout {

    RestaurantLayout() {
        setPrimarySection(Section.DRAWER);
        addToDrawer(createHeader(), new Scroller(createSideNav()), createUserMenu());
    }

    private Div createHeader() {
        Image appLogo = new Image("./logos/LOGO.png", "Snacktrack Logo");
        appLogo.addClassNames(LumoUtility.TextColor.PRIMARY, LumoUtility.IconSize.LARGE);

        var appName = new Span("Snacktrack");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);

        var header = new Div(appLogo, appName);
        header.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Padding.MEDIUM, LumoUtility.Gap.MEDIUM, LumoUtility.AlignItems.CENTER);
        return header;
    }

    private SideNav createSideNav() {
        var nav = new SideNav();
        nav.addClassNames(LumoUtility.Margin.Horizontal.MEDIUM);
        MenuConfiguration.getMenuEntries().forEach(entry -> nav.addItem(createSideNavItem(entry)));
        nav.addItem(new SideNavItem("Open Orders", "restaurant-home", new Icon(VaadinIcon.HOME)));
        nav.addItem(new SideNavItem("Menu", "restaurant-menu", new Icon(VaadinIcon.MENU)));
        nav.addItem(new SideNavItem("Completed Orders", "restaurant-completed-orders", new Icon(VaadinIcon.CHECK)));
        nav.addItem(new SideNavItem("Report", "restaurant-report", new Icon(VaadinIcon.DIAMOND)));
        return nav;
    }

    private SideNavItem createSideNavItem(MenuEntry menuEntry) {
        if (menuEntry.icon() != null) {
            return new SideNavItem(menuEntry.title(), menuEntry.path(), new Icon(menuEntry.icon()));
        } else {
            return new SideNavItem(menuEntry.title(), menuEntry.path());
        }
    }

    private Component createUserMenu() {
        Restaurant restaurant = (Restaurant) VaadinSession.getCurrent().getAttribute("user");

        String restaurantName = restaurant != null
                ? restaurant.getRestaurantName()
                : "Unknown Restaurant";

        var avatar = new Avatar(restaurantName);
        avatar.addThemeVariants(AvatarVariant.LUMO_XSMALL);
        avatar.addClassNames(LumoUtility.Margin.Right.SMALL);
        avatar.setColorIndex(5);

        var userMenu = new MenuBar();
        userMenu.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
        userMenu.addClassNames(LumoUtility.Margin.MEDIUM);

        var userMenuItem = userMenu.addItem(avatar);
        userMenuItem.add(restaurantName);
        // Navigate to Restaurant Profile
        userMenuItem.getSubMenu().addItem("View Profile", e -> {
            UI.getCurrent().navigate("restaurant-profile");
        });
        // Logout User
        userMenuItem.getSubMenu().addItem("Logout", e -> {
            VaadinSession.getCurrent().close();    // clear session
            UI.getCurrent().navigate("");   // navigate to loginView
        });

        return userMenu;
    }
}
