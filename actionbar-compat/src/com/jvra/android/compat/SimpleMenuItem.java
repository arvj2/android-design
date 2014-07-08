package com.jvra.android.compat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.*;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 5/30/14.
 */
public class SimpleMenuItem implements MenuItem {

    private SimpleMenu menu;
    private int id;
    private CharSequence title;
    private CharSequence titleCondensed;
    private Drawable iconDrawable;
    private int iconRes;
    private int order;
    private boolean enabled;


    public SimpleMenuItem( SimpleMenu menu, int itemId,int order,CharSequence title ){
        this.menu = menu;
        this.id = itemId;
        this.title = title;
    }

    @Override
    public int getItemId() {
        return id;
    }

    @Override
    public int getGroupId() {
        return 0;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public MenuItem setTitle(CharSequence sequence) {
        this.title = sequence;
        return this;
    }

    @Override
    public MenuItem setTitle(int i) {
        this.title = menu.getResources().getString(i);
        return this;
    }

    @Override
    public CharSequence getTitle() {
        return title;
    }

    @Override
    public MenuItem setTitleCondensed(CharSequence sequence) {
        this.titleCondensed = sequence;
        return this;
    }

    @Override
    public CharSequence getTitleCondensed() {
        return null == titleCondensed ? title : titleCondensed;
    }

    @Override
    public MenuItem setIcon(Drawable drawable) {
        this.iconDrawable = drawable;
        return this;
    }

    @Override
    public MenuItem setIcon(int i) {
        iconRes = i;
        iconDrawable = menu.getResources().getDrawable(i);
        return this;
    }

    @Override
    public Drawable getIcon() {
        return iconDrawable;
    }

    @Override
    public MenuItem setIntent(Intent intent) {
        return null;
    }

    @Override
    public Intent getIntent() {
        return null;
    }

    @Override
    public MenuItem setShortcut(char c, char c2) {
        return null;
    }

    @Override
    public MenuItem setNumericShortcut(char c) {
        return null;
    }

    @Override
    public char getNumericShortcut() {
        return 0;
    }

    @Override
    public MenuItem setAlphabeticShortcut(char c) {
        return null;
    }

    @Override
    public char getAlphabeticShortcut() {
        return 0;
    }

    @Override
    public MenuItem setCheckable(boolean b) {
        return null;
    }

    @Override
    public boolean isCheckable() {
        return false;
    }

    @Override
    public MenuItem setChecked(boolean b) {
        return null;
    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public MenuItem setVisible(boolean b) {
        return null;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public MenuItem setEnabled(boolean b) {
        enabled = b;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean hasSubMenu() {
        return false;
    }

    @Override
    public SubMenu getSubMenu() {
        return null;
    }

    @Override
    public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        return null;
    }

    @Override
    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return null;
    }

    @Override
    public void setShowAsAction(int i) {

    }

    @Override
    public MenuItem setShowAsActionFlags(int i) {
        return null;
    }

    @Override
    public MenuItem setActionView(View view) {
        return null;
    }

    @Override
    public MenuItem setActionView(int i) {
        return null;
    }

    @Override
    public View getActionView() {
        return null;
    }

    @Override
    public MenuItem setActionProvider(ActionProvider actionProvider) {
        return null;
    }

    @Override
    public ActionProvider getActionProvider() {
        return null;
    }

    @Override
    public boolean expandActionView() {
        return false;
    }

    @Override
    public boolean collapseActionView() {
        return false;
    }

    @Override
    public boolean isActionViewExpanded() {
        return false;
    }

    @Override
    public MenuItem setOnActionExpandListener(OnActionExpandListener onActionExpandListener) {
        return null;
    }
}
