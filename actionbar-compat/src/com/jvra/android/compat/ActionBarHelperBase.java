package com.jvra.android.compat;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 5/30/14.
 */
public class ActionBarHelperBase extends ActionBarHelper {
    private static final String MENU_NAMESPACE = "http://schemas.android.com/apk/res/android";
    private static final String ATTR_ID = "id";
    private static final String MNEU_ATTR_SHOW_AS_ACTION = "showAsAction";


    private Set<Integer> actionsItemsIds = new HashSet<Integer>();


    public ActionBarHelperBase(Activity activity) {
        super(activity);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getActivity().requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        getActivity().getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.actionbar_compat);
        setupActionBar();

        SimpleMenu menu = new SimpleMenu(getActivity());
        getActivity().onCreatePanelMenu(Window.FEATURE_OPTIONS_PANEL, menu);
        getActivity().onCreateOptionsMenu(menu);
        for (int i = 0; menu.size() > i; ++i) {
            MenuItem item = menu.getItem(i);
            addActionItemCompatFromMenuItem(item);
        }
    }


    @Override
    protected void onRefreshActionItemState(boolean refreshing) {
        View refreshButton = getActivity().findViewById(R.id.actionbar_compat_item_refresh);
        View refreshButtonIndicator = getActivity().findViewById(R.id.actionbar_compat_item_refresh_progress);

        if (null != refreshButtonIndicator)
            refreshButtonIndicator.setVisibility(refreshing ? View.VISIBLE : View.GONE);

        if (null != refreshButton)
            refreshButton.setVisibility(refreshing ? View.GONE : View.VISIBLE);
    }


    private void setupActionBar() {
        final ViewGroup actionbar = getActiobarCompat();
        if (null == actionbar)
            return;

        LinearLayout.LayoutParams springLayoutParams = new LinearLayout.LayoutParams(0, ActionBar.LayoutParams.FILL_PARENT);
        springLayoutParams.weight = 1;

        SimpleMenu tempMenu = new SimpleMenu(getActivity());
        SimpleMenuItem homeItem = new SimpleMenuItem(tempMenu, android.R.id.home, 0, getActivity().getString(R.string.app_name));
        homeItem.setIcon(R.drawable.ic_home);

        addActionItemCompatFromMenuItem(homeItem);

        TextView title = new TextView(getActivity(), null, R.attr.actionbarCompatTitleStyle);
        title.setLayoutParams(springLayoutParams);
        title.setText(getActivity().getTitle());
        actionbar.addView(title);
    }


    @Override
    protected boolean onCreateOptionsMenu(Menu menu) {
        for (Integer id : actionsItemsIds)
            menu.findItem(id).setVisible(false);
        return true;
    }


    @Override
    protected void onTitleChange(CharSequence title, int color) {
        super.onTitleChange(title, color);
        TextView actionbarTitle = (TextView) getActivity().findViewById(R.id.actionbar_compat_title);

        if (null != actionbarTitle)
            actionbarTitle.setText(title);
    }


    @Override
    public MenuInflater getMenuInflater(MenuInflater inflater) {
        return new WrappedMenuInflater(getActivity(), inflater);
    }


    private ViewGroup getActiobarCompat() {
        return (ViewGroup) getActivity().findViewById(R.id.actionbar_compat);
    }


    private void addActionItemCompatFromMenuItem(final MenuItem item) {
        final int itemId = item.getItemId();

        final ViewGroup actionbar = getActiobarCompat();
        if (null == actionbar)
            return;

        ImageButton button = new ImageButton(getActivity(), null,
                itemId == android.R.id.home ?
                        R.attr.actionbarCompatItemHomeStyle :
                        R.attr.actionbarCompatItemStyle);

        int width = (int) getActivity().getResources().getDimension(
                itemId == android.R.id.home ?
                        R.dimen.actionbar_compat_button_home_width :
                        R.dimen.actionbar_compat_button_width
        );

        button.setLayoutParams(new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.FILL_PARENT));
        if (itemId == R.id.menu_refresh)
            button.setId(R.id.actionbar_compat_item_refresh);

        button.setImageDrawable(item.getIcon());
        button.setScaleType(ImageView.ScaleType.CENTER);
        button.setContentDescription(item.getTitle());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onMenuItemSelected(Window.FEATURE_OPTIONS_PANEL, item);
            }
        });

        actionbar.addView(button);

        if (itemId == R.id.menu_refresh) {
            ProgressBar refreshIndicator = new ProgressBar(getActivity(), null, R.attr.actionbarCompatProgressIndicatorStyle);

            final int buttonWidth = (int) getActivity().getResources().getDimension(R.dimen.actionbar_compat_button_width);
            final int buttonHeight = (int) getActivity().getResources().getDimension(R.dimen.actionbar_compat_height);

            final int progressIndicatorWidth = buttonWidth / 2;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(buttonWidth, buttonHeight);
            params.setMargins(
                    (buttonHeight - progressIndicatorWidth) / 2,
                    (buttonHeight - progressIndicatorWidth) / 2,
                    (buttonHeight - progressIndicatorWidth) / 2,
                    0);

            refreshIndicator.setLayoutParams(params);
            refreshIndicator.setVisibility(View.GONE);
            refreshIndicator.setId(R.id.actionbar_compat_item_refresh_progress);
            actionbar.addView(refreshIndicator);
        }
    }


    private class WrappedMenuInflater extends MenuInflater {
        private MenuInflater inflater;

        private WrappedMenuInflater(Context context, MenuInflater inflater) {
            super(context);
            this.inflater = inflater;
            Log.e( "*********","one: "+inflater );
        }

        @Override
        public void inflate(int menuRes, Menu menu) {
            loadActionbarMetadata(menuRes);
            Log.e( "*********","two: "+menu );
            inflater.inflate(menuRes, menu);
        }

        private void loadActionbarMetadata(int resMenu) {
            XmlResourceParser parser = null;
            try {
                parser = getActivity().getResources().getXml(resMenu);

                int eventType = parser.getEventType();
                int itemId;
                int showAsAction;

                boolean eof = false;
                while (!eof) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG: {
                            if (!parser.getName().equals("item")) {
                                break;
                            }
                            itemId = parser.getAttributeResourceValue(MENU_NAMESPACE, ATTR_ID, 0);
                            if (0 == itemId)
                                break;

                            showAsAction = parser.getAttributeResourceValue(MENU_NAMESPACE, MNEU_ATTR_SHOW_AS_ACTION, -1);
                            if (showAsAction == MenuItem.SHOW_AS_ACTION_ALWAYS || showAsAction == MenuItem.SHOW_AS_ACTION_IF_ROOM)
                                actionsItemsIds.add(itemId);
                        }
                        case XmlPullParser.END_DOCUMENT:
                            eof = true;
                            break;

                    }

                    eventType = parser.next();
                }
            } catch (XmlPullParserException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (null != parser)
                    parser.close();
            }
        }
    }
}
