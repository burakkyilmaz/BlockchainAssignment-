package com.packagename.myapp.presenter.main;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.packagename.myapp.enums.Parti;
import com.packagename.myapp.model.KisiselPartiBilgileri;
import com.packagename.myapp.model.KriptoBlock;
import com.packagename.myapp.service.kriptoblock.KriptoBlockService;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;

@Component
@Scope("prototype")

public class MainPresenter {

	@Autowired
	KriptoBlockService kriptoViewService;

	public DataSeries getOyKullanimDataSeries() {
		List<KriptoBlock> blokList = kriptoViewService.getBlokList();

		Integer aPartiCount = 0;
		Integer bPartiCount = 0;
		Integer cPartiCount = 0;

		for (KriptoBlock kriptoBlock : blokList) {
			if (kriptoBlock.getValue().contains(Parti.A_PARTISI.getCode())) {
				aPartiCount++;
			}

			else if (kriptoBlock.getValue().contains(Parti.B_PARTISI.getCode())) {
				bPartiCount++;
			} else if (kriptoBlock.getValue().contains(Parti.C_PARTISI.getCode())) {
				cPartiCount++;
			}
		}

		Integer toplamCount = aPartiCount + bPartiCount + cPartiCount;

		if (toplamCount.equals(0)) {
			toplamCount = 1;
		}

		DataSeries series = new DataSeries();
		DataSeriesItem chrome = new DataSeriesItem("A Partisi", aPartiCount / toplamCount);
		chrome.setSliced(true);
		chrome.setSelected(true);
		series.add(chrome);

		series.add(new DataSeriesItem("B Partisi", bPartiCount / toplamCount));
		series.add(new DataSeriesItem("C Partisi", cPartiCount / toplamCount));

		return series;

	}

	public Boolean isOyKullanmamis(String tcKimlikNo) {
		List<KriptoBlock> blokList = kriptoViewService.getBlokList();
		for (KriptoBlock kriptoBlock : blokList) {
			if (kriptoBlock.getValue().contains(tcKimlikNo)) {
				return true;

			}

		}
		return false;

	}

	public void addBlock(KisiselPartiBilgileri kisiselPartiBilgileri) {
		kriptoViewService.addBlock(
				kriptoViewService.newBlock(kisiselPartiBilgileri.getTcKimlikNo() + " " + kisiselPartiBilgileri.getDogumYili() + " "

						+ kisiselPartiBilgileri.getPartiSecimi().getCode()));

	}

}
