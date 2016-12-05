package com.capstone.tip.emassage.ui.pdf;

import android.util.Log;

import com.capstone.tip.emassage.app.App;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author pocholomia
 * @since 05/12/2016
 */

public class PdfPresenter extends MvpNullObjectBasePresenter<PdfView> {

    private static final String TAG = PdfPresenter.class.getSimpleName();

    public void loadPdf(final String pdf) {
        getView().startLoading();
        App.getInstance().getMediaApiInterface().getLessonPdf(pdf).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getView().stopLoading();
                if (response.isSuccessful()) {
                    if (writeResponseBodyToDisk(response.body(), pdf)) {
                        getView().loadPdfLocal();
                    } else {
                        getView().showAlert("Error Saving PDF Locally");
                    }
                } else {
                    try {
                        getView().showAlert(response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: Error parsing error body", e);
                        getView().showAlert(response.message() != null ? response.message()
                                : "Unknown Error");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: Error Calling PDF API", t);
                getView().stopLoading();
                getView().showAlert("Error Calling API");
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String pdf) {
        try {
            File file = new File(App.getInstance().getApplicationContext().getFilesDir(),
                    pdf.replace("/media/", ""));

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

}
