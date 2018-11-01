/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package android.example.com.squawker.following;

import android.content.SharedPreferences;
import android.example.com.squawker.R;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;


/**
 * Shows the list of instructors you can follow
 */
// DONE (1) Implement onSharedPreferenceChangeListener
public class FollowingPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final static String LOG_TAG = FollowingPreferenceFragment.class.getSimpleName();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Add visualizer preferences, defined in the XML file in res->xml->preferences_squawker
        addPreferencesFromResource(R.xml.following_squawker);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String asser = getString(R.string.follow_key_switch_asser);
        String cezanne = getString(R.string.follow_key_switch_cezanne);
        String jlin = getString(R.string.follow_key_switch_jlin);
        String lyla = getString(R.string.follow_key_switch_lyla);
        String nikita = getString(R.string.follow_key_switch_nikita);

        // DONE (2) When a SharedPreference changes, check which preference it is and subscribe or
        // un-subscribe to the correct topics.

        // Ex. FirebaseMessaging.getInstance().subscribeToTopic("key_lyla");
        // subscribes to Lyla's squawks.

        // HINT: Checkout res->xml->following_squawker.xml. Note how the keys for each of the
        // preferences matches the topic to subscribe to for each instructor.

        boolean result = false;
        if (key.equals(asser)) {
            result = sharedPreferences.getBoolean(asser, false);
            handleSubscriptionToKey(asser, result);
        } else if (key.equals(cezanne)) {
            result = sharedPreferences.getBoolean(cezanne, false);
            handleSubscriptionToKey(cezanne, result);
        } else if (key.equals(jlin)) {
            result = sharedPreferences.getBoolean(jlin, false);
            handleSubscriptionToKey(jlin, result);
        } else if (key.equals(lyla)) {
            result = sharedPreferences.getBoolean(lyla, false);
            handleSubscriptionToKey(lyla, result);
        } else if (key.equals(nikita)) {
            result = sharedPreferences.getBoolean(nikita, false);
            handleSubscriptionToKey(nikita, result);
        }

        // DONE (3) Make sure to register and unregister this as a Shared Preference Change listener, in
        // onCreate and onDestroy.
    }

    private void handleSubscriptionToKey(String key, boolean isSubscribed) {
        if (isSubscribed) {
            FirebaseMessaging.getInstance().subscribeToTopic(key);
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(key);
        }
    }

}
