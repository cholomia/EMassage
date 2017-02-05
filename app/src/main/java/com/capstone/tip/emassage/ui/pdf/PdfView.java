package com.capstone.tip.emassage.ui.pdf;

import com.capstone.tip.emassage.model.data.Lesson;
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

    void onTakeQuiz(Lesson lesson);

    void onViewVideo(Lesson lesson);

    void onBack();

    void onMenu();

    void onNext();
}
