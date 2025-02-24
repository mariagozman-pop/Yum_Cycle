// com/example/yumcycle/FirstFragment.java
package com.example.yumcycle;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable navigateRunnable;
    private static final long DELAY_MILLIS = 5000; // 5 seconds

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Define the Runnable to navigate to SecondFragment
        navigateRunnable = new Runnable() {
            @Override
            public void run() {
                // Ensure that the fragment is still attached before navigating
                if (isAdded()) {
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);
                }
            }
        };

        // Post the Runnable with a delay
        handler.postDelayed(navigateRunnable, DELAY_MILLIS);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Remove any pending posts of navigateRunnable to prevent memory leaks
        handler.removeCallbacks(navigateRunnable);
    }
}
