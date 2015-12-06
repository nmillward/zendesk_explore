package com.superlifesize.zendeskexploration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zendesk.sdk.feedback.impl.BaseZendeskFeedbackConfiguration;
import com.zendesk.sdk.feedback.ui.ContactZendeskActivity;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.requests.RequestActivity;
import com.zendesk.sdk.support.SupportActivity;
import com.zendesk.service.ErrorResponse;
import com.zendesk.service.ZendeskCallback;

public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();

    Button btn_kb, btn_contact_us, btn_my_tickets, btn_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Zendesk User Auth
        setupUserAuth();

        // Instantiate Zendesk SDK
        setupZendesk();

        // Instantiate Menu Tree
        setupMainActivity();

    }

    private void setupMainActivity() {
        btn_kb = (Button) findViewById(R.id.btn_knowledge_base);
        btn_contact_us = (Button) findViewById(R.id.btn_contact_us);
        btn_my_tickets = (Button) findViewById(R.id.btn_my_tickets);
        btn_chat = (Button) findViewById(R.id.btn_start_chat);

        // Knowledge Base
        btn_kb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SupportActivity.Builder().show(MainActivity.this);
            }
        });

        // Contact Us
        btn_contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ContactZendeskActivity.class);
                startActivity(i);
            }
        });

        // My Tickets
        btn_my_tickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RequestActivity.class);
                startActivity(i);
            }
        });

        // Start Chat
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "No Chats Available", Toast.LENGTH_SHORT).show();
//                PreChatForm build = new PreChatForm.Builder()
//                        .name(PreChatForm.Field.REQUIRED)
//                        .email(PreChatForm.Field.REQUIRED)
//                        .phoneNumber(PreChatForm.Field.OPTIONAL)
//                        .message(PreChatForm.Field.OPTIONAL)
//                        .build();
//
//                ZopimChat.SessionConfig department = new ZopimChat.SessionConfig()
//                        .preChatForm(build)
//                        .department("The date");
//
//                ZopimChatActivity.startActivity(getActivity(), department);
            }
        });
    }

    private void setupZendesk() {
        ZendeskConfig.INSTANCE.init(this,
                ZendeskConstants.ZENDESK_URL, ZendeskConstants.APPLICATION_ID, ZendeskConstants.OAUTH_CLIENT_ID,
                new ZendeskCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        ZendeskConfig.INSTANCE.setContactConfiguration(new BaseZendeskFeedbackConfiguration() {
                            @Override
                            public String getRequestSubject() {
                                Log.d(TAG, "Successfully setup Zendesk");
                                return "Support Request";
                            }
                        });
                    }

                    @Override
                    public void onError(ErrorResponse errorResponse) {
                        Log.d(TAG, "Failed to setup Zendesk");
                    }
                });
    }

    private void setupUserAuth() {
        // Anonymous User Identification
        Identity anonId = new AnonymousIdentity.Builder().build();
        ZendeskConfig.INSTANCE.setIdentity(anonId);

//        Configuration for JWT Authentication
//         NOTE: Must be instantiated BEFORE ZendeskConfig.INSTANCE.init
//         Identity jwtIdentity = new JwtIdentity("nick-test-user");
//         ZendeskConfig.INSTANCE.setIdentity(jwtIdentity);
    }


}
