package com.packagename.myapp.view.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.packagename.myapp.data.converter.PartiStringToIntegerConverter;
import com.packagename.myapp.enums.Parti;
import com.packagename.myapp.model.KisiselPartiBilgileri;
import com.packagename.myapp.presenter.main.MainPresenter;
import com.packagename.myapp.service.kriptoblock.KriptoBlockService;
import com.packagename.myapp.view.ReCaptcha.ReCaptcha;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.Cursor;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.PlotOptionsPie;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and use @Route
 * annotation to announce it in a URL as a Spring managed bean. Use the @PWA
 * annotation make the application installable on phones, tablets and some
 * desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every browser
 * tab/window.
 */
@Component
@Route
@Scope("prototype")
@PWA(name = "Vaadin Application", shortName = "Vaadin App", description = "This is an example Vaadin application.", enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

	@Autowired
	KriptoBlockService kriptoViewService;

	@Autowired
	MainPresenter presenter;

	// @formatter:off

	private FlexLayout	mainLayout;
		private FlexLayout headerLayout;
		private Image imageLogo;
		private Label headerLabel;
		private FlexLayout firstLayout;
			private TextField tcKimlikNoField;
			private TextField dogumYiliField;
			private ComboBox<Parti> secimCombobox;
			private ReCaptcha recaptcha;
			private Button onayButton;
		private FlexLayout secondLayout;
			private Chart chart;
	// @formatter:on

	Binder<KisiselPartiBilgileri> binder;

	KisiselPartiBilgileri kisiselPartiBilgileri = new KisiselPartiBilgileri();

	Dialog dialog = new Dialog();

	private void onayButtonClicked() {
		dialog.removeAll();
		kriptoViewService.setDifficulty(4);
		if (binder.isValid()) {

			if (recaptcha.isValid()) {

				if (presenter.isOyKullanmamis(kisiselPartiBilgileri.getTcKimlikNo())) {

					FlexLayout dialogLayout = new FlexLayout();
					dialogLayout.getStyle().set("flex-direction", "column");

					Label oyUyariLabel = new Label("Daha önce oy kullanmışsınız.Tekrar oy kullanamazsınız!");
					Button buttonKapat = new Button("Kapat");
					buttonKapat.getStyle().set("margin-left", "%80");
					buttonKapat.setThemeName("primary");
					buttonKapat.addClickListener(e -> closeDialog());

					dialogLayout.add(oyUyariLabel, buttonKapat);

					dialog.add(dialogLayout);
					dialog.open();

				} else {
					presenter.addBlock(kisiselPartiBilgileri);

					FlexLayout dialogLayout = new FlexLayout();
					dialogLayout.getStyle().set("flex-direction", "column");

					Label oyUyariLabel = new Label("Oy Kullanma İşlemi Başarıyla gerçekleştirildi");
					Button buttonKapat = new Button("Kapat");
					buttonKapat.getStyle().set("margin-left", "%80");
					buttonKapat.setThemeName("primary success");
					buttonKapat.addClickListener(e -> closeDialog());

					dialogLayout.add(oyUyariLabel, buttonKapat);

					dialog.add(dialogLayout);
					dialog.open();

					configChart(); // update series
					drawChart(); // çiz

					clearComponent();

					System.out.println("Blockchain valid: " + kriptoViewService.validateBlockchain());
					System.out.println(kriptoViewService.toString());

				}

			}

			else {
				Notification.show("ReCaptcha doğrulamasını yapınız.", 2000, Position.MIDDLE);
			}
		} else {

			List<ValidationResult> errors = binder.validate().getValidationErrors();
			String errorMessage = errors.stream().map(ValidationResult::getErrorMessage)
					.map(errorString -> Jsoup.clean(errorString, Whitelist.simpleText())).collect(Collectors.joining("\n"));

			Notification.show(errorMessage, 2000, Position.MIDDLE);
		}
	}

	private void clearComponent() {
		binder.removeBean();
		tcKimlikNoField.clear();
		dogumYiliField.clear();
		secimCombobox.clear();

	}

	private void closeDialog() {
		dialog.close();
	}

	private void initBinder() {

		if (binder != null) {
			binder.setBean(null);
		}

		binder = new Binder<>();

		binder.forField(tcKimlikNoField)
				.withValidator(value -> tcKimlikNoField.getValue().length() == 11, "TC Kimlik No/Pasaport No eksik girildi.")
				.bind(KisiselPartiBilgileri::getTcKimlikNo, KisiselPartiBilgileri::setTcKimlikNo);

		binder.forField(dogumYiliField)
				.withValidator(value -> dogumYiliField.getValue().length() == 4, "Lütfen 4 haneli doğum yılınızı giriniz")
				.withValidator(value -> Integer.parseInt(dogumYiliField.getValue()) > (LocalDate.now().getYear() - 200),
						"Doğum Yılı bilgisi yanlış girildi")
				.withValidator(value -> Integer.parseInt(dogumYiliField.getValue()) < (LocalDate.now().getYear() + 1),
						"Doğum Yılı bilgisi yanlış girildi")
				.withConverter(new PartiStringToIntegerConverter("Rakam girmelisiniz"))
				.bind(KisiselPartiBilgileri::getDogumYili, KisiselPartiBilgileri::setDogumYili);

		binder.forField(secimCombobox).withValidator(value -> secimCombobox.getValue() != null, "Seçim yapmalısın!")
				.bind(KisiselPartiBilgileri::getPartiSecimi, KisiselPartiBilgileri::setPartiSecimi);

		binder.setBean(kisiselPartiBilgileri);

	}

	private void fillItems(ComboBox<Parti> doldurulacakComBobox) {
		ArrayList<Parti> partiList = new ArrayList<>();

		for (Parti parti : EnumSet.allOf(Parti.class)) {
			partiList.add(parti);

		}

		doldurulacakComBobox.setItems(partiList);

	}

	@PostConstruct
	public void init() {

		buildUI();
		initBinder();
	}

	private void buildUI() {
		setMargin(false);
		setPadding(false);

		getStyle().set("background-color", "var(--lumo-shade-5pct)");
		setWidthFull();
		setHeightFull();
		getStyle().set("justify-content", "center");
		getStyle().set("min-height", "450px");
		getStyle().set("align-items", "center");
		getStyle().set("padding-top", "30px");
		getStyle().set("padding-bottom", "30px");

		headerLayout = new FlexLayout();
		headerLayout.getStyle().set("flex-direction", "row");
		// headerLayout.setAlignItems(Alignment.CENTER);

		headerLayout.getStyle().set("margin-left", "auto");
		headerLayout.getStyle().set("margin-right", "auto");
		headerLayout.getStyle().set("align-items", "center");

		imageLogo = new Image();
		imageLogo.setSrc("./images/logo.jpg");
		imageLogo.setWidth("170px");
		imageLogo.setHeight("140px");

		headerLabel = new Label();
		headerLabel.setText("Seçim Anketi İçin Oy Kullanma Sayfası");
		headerLabel.getElement().getStyle().set("font-size", "var(--lumo-font-size-xxl)");
		headerLabel.getElement().getStyle().set("font-weight", "bold");

		mainLayout = new FlexLayout();
		mainLayout.getStyle().set("flex-direction", "column");
		mainLayout.getStyle().set("margin-left", "auto");
		mainLayout.getStyle().set("margin-right", "auto");
		mainLayout.getStyle().set("margin-right", "auto");

		mainLayout.getStyle().set("box-shadow", "var(--lumo-box-shadow-l)");
		mainLayout.getStyle().set("border-radius", "var(--lumo-border-radius-l)");
		mainLayout.getStyle().set("align-items", "center");
		mainLayout.getStyle().set("background-color", "white");
		mainLayout.getStyle().set("padding-left", "30px");
		mainLayout.getStyle().set("padding-right", "30px");

		firstLayout = new FlexLayout();
		firstLayout.getStyle().set("flex-direction", "column");
		firstLayout.setWidthFull();

		secondLayout = new FlexLayout();
		secondLayout.getStyle().set("flex-direction", "row");
		secondLayout.setWidthFull();

		tcKimlikNoField = new TextField();
		tcKimlikNoField.setPattern("[0-9]*");
		tcKimlikNoField.setPreventInvalidInput(true);
		tcKimlikNoField.setPlaceholder("Tc Kimlik No:");
		tcKimlikNoField.setMinLength(0);
		tcKimlikNoField.setMaxLength(11);
		tcKimlikNoField.setLabel("Tc Kimlik No");
		tcKimlikNoField.setRequired(true);

		dogumYiliField = new TextField();
		dogumYiliField.setPreventInvalidInput(true);
		dogumYiliField.setPattern("[0-9]*");
		dogumYiliField.setPlaceholder("Doğum Yılınız");
		dogumYiliField.setLabel("Doğum Yılınız");
		dogumYiliField.setMinLength(0);
		dogumYiliField.setMaxLength(4);
		dogumYiliField.setRequired(true);

		secimCombobox = new ComboBox<>();
		secimCombobox.setLabel("Seçiminiz");
		secimCombobox.setPlaceholder("Seçiminiz");
		secimCombobox.setRequired(true);
		secimCombobox.getElement().getStyle().set("margin-bottom", "10px");
		fillItems(secimCombobox);

		recaptcha = new ReCaptcha("6LfBw_sUAAAAAK6Qewbu72G9FyNS8hraJL75e_NQ", "6LfBw_sUAAAAAFm1cekVB2G7q8YNCSfAvbFbLnFH");
		recaptcha.getElement().getStyle().set("margin-left", "auto");
		recaptcha.getElement().getStyle().set("margin-right", "auto");

		onayButton = new Button();
		onayButton.setThemeName("primary success");
		onayButton.setText("Oy Kullan");
		onayButton.getElement().getStyle().set("margin-top", "10px");
		onayButton.addClickListener(e -> onayButtonClicked());

		headerLayout.add(imageLogo, headerLabel);

		firstLayout.add(tcKimlikNoField, dogumYiliField, secimCombobox, recaptcha, onayButton);

		chart = new Chart(ChartType.PIE);
		chart.setMaxHeight("450px");
		chart.setHeight("auto");

		configChart();

		secondLayout.add(chart);

		mainLayout.add(headerLayout, firstLayout, secondLayout);

		add(mainLayout);
	}

	private void configChart() {
		Configuration conf = chart.getConfiguration();

		conf.setTitle("Oy Dağılımı");

		Tooltip tooltip = new Tooltip();
		tooltip.setValueDecimals(1);
		conf.setTooltip(tooltip);

		PlotOptionsPie plotOptions = new PlotOptionsPie();
		plotOptions.setAllowPointSelect(true);
		plotOptions.setCursor(Cursor.POINTER);
		plotOptions.setShowInLegend(true);
		conf.setPlotOptions(plotOptions);

		DataSeries series = presenter.getOyKullanimDataSeries();
		conf.setSeries(series);
		chart.setVisibilityTogglingDisabled(true);
	}

	private void drawChart() {
		chart.drawChart();
	}

}
