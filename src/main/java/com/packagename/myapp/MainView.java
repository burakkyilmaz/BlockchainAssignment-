package com.packagename.myapp;


import com.packagename.myapp.view.ReCaptcha.ReCaptcha;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WebBrowser;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 */
@Route
@PWA(name = "Vaadin Application",
        shortName = "Vaadin App",
        description = "This is an example Vaadin application.",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

	// @formatter:off
	private FlexLayout rootLayout;
		private FlexLayout mainLayout;
			private TextField tcKimlikveyaPasaportTextField;
			private ReCaptcha recaptchaV2;
			private Button loginButton;
	// @formatter:on
	
    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service The message service. Automatically injected Spring managed bean.
     */
    public MainView(@Autowired GreetService service) {

        // Use TextField for standard text input
        TextField textField = new TextField("Your name");

        // Button click listeners can be defined as lambda expressions
        Button button = new Button("Say hello",
                e -> Notification.show(service.greet(textField.getValue())));

        // Theme variants give you predefined extra styles for components.
        // Example: Primary button is more prominent look.
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        button.addClickShortcut(Key.ENTER);

        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");

        add(textField, button);
    }
    
	@PostConstruct
	public void init() {

		
			buildUI();
		}

	private void buildUI() {
		rootLayout.setSizeFull();
		rootLayout.setFlexDirection(FlexDirection.COLUMN);
		rootLayout.setJustifyContentMode(JustifyContentMode.CENTER);
		rootLayout.setAlignItems(Alignment.CENTER);
		rootLayout.setMinHeight("450px");
		UIUtils.setTextColor(TextColor.BODY, rootLayout);
		UIUtils.setBackgroundColor(LumoStyles.Color.Shade._5, rootLayout);

		mainLayout = new FlexBoxLayout();

		mainLayout = new FlexBoxLayout();
		mainLayout.setFlexDirection(FlexDirection.COLUMN);
		mainLayout.setAlignItems(Alignment.CENTER);
		mainLayout.addClassName("hasta-portal-randevu-login-view__mainLayout");
		UIUtils.setBackgroundColor(LumoStyles.Color.BASE_COLOR, mainLayout);
		mainLayout.setBorderRadius(BorderRadius.L);
		UIUtils.setShadow(Shadow.L, mainLayout);
		
	}
	

}
