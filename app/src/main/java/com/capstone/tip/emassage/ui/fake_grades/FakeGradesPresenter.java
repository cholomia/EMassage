package com.capstone.tip.emassage.ui.fake_grades;

import com.capstone.tip.emassage.model.data.Category;
import com.capstone.tip.emassage.model.pojo.FakeGrade;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

public class FakeGradesPresenter extends MvpNullObjectBasePresenter<FakeGradesView> {

    private Realm realm;
    private RealmResults<Category> categoryRealmResults;

    void start() {
        realm = Realm.getDefaultInstance();
        categoryRealmResults = realm.where(Category.class).findAllAsync();
        categoryRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Category>>() {
            @Override
            public void onChange(RealmResults<Category> element) {
                if (categoryRealmResults.isLoaded() && categoryRealmResults.isValid())
                    createFakeGrades(realm.copyFromRealm(categoryRealmResults));
            }
        });
    }

    void stop() {
        categoryRealmResults.removeChangeListeners();
        realm.close();
    }

    private void createFakeGrades(List<Category> categories) {
        List<FakeGrade> fakeGrades = new ArrayList<>();
        for (Category category : categories) {
            FakeGrade fakeGrade = new FakeGrade(category.getTitle(),
                    getRandomAve(), getRandomAve(), getRandomAve());
            fakeGrades.add(fakeGrade);
        }
        getView().setFakeGrades(fakeGrades);
    }

    private double getRandomAve() {
        Random random = new Random();
        return random.nextInt((100 - 75) + 1) + 75.0;
    }

}
