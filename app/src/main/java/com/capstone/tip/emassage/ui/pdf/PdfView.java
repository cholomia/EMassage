package com.capstone.tip.emassage.ui.pdf;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * @author pocholomia
 * @since 05/12/2016
 */

public interface PdfView extends MvpView {
    void startLoading();

    void stopLoading();

    void showAlert(String message);

    void loadPdfLocal();
}
