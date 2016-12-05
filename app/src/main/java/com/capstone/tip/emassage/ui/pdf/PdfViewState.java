package com.capstone.tip.emassage.ui.pdf;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * @author pocholomia
 * @since 05/12/2016
 */

public class PdfViewState implements RestorableViewState<PdfView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<PdfView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(PdfView view, boolean retained) {

    }
}
