package com.example.alex.realmdemo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.alex.realmdemo.data.User;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    private final RealmChangeListener<RealmResults<User>> changeListener = new RealmChangeListener<RealmResults<User>>() {
        @Override
        public void onChange(RealmResults<User> elements) {
            updateUI(elements);
        }
    };
    private RecyclerView rvUsers;
    private Realm realm;

    private void updateUI(RealmResults<User> elements) {
        if (rvUsers.getAdapter() == null) {
            rvUsers.setAdapter(new UsersAdapter(elements));
        } else {
            UsersAdapter adapter = (UsersAdapter) rvUsers.getAdapter();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rvUsers = (RecyclerView) findViewById(R.id.rvUsers);

        Button btnAdd = (Button) findViewById(R.id.btnAddUsers);
        Button btnSave = (Button) findViewById(R.id.btnSaveUsers);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUsers(getDummyUsers());
            }
        });

        realm = Realm.getDefaultInstance();

        final RealmResults<User> users = realm.where(User.class)
                .findAllAsync();
        users.addChangeListener(changeListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void saveUsers(final List<User> users) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        //realm.insertOrUpdate(users);
        realm.commitTransaction();
        realm.close();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(users);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {

            }
        });
    }

    @NonNull
    private List<User> getDummyUsers() {
        final List<User> users = new ArrayList<>();

        int min = 0;
        RealmResults<User> rawUsers = realm.where(User.class).findAllSorted("id", Sort.DESCENDING);
        if (rawUsers.size() > 0) {
            min = rawUsers.first().getId();
        }

        for (int i = min + 1; i < min + 3; i++) {
            User user = new User();
            user.setId(i);
            user.setName("User #" + i);
            users.add(user);
        }
        return users;
    }
}
