package co.websarva.wings.android.myquotations.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MyFragment extends Fragment {

    public static Context getInstance() {
        Context context = new MyFragment().getActivity();
        return context;
    }
}