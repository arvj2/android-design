package com.jvra.android.compat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 5/30/14.
 */
public class SimpleMenu implements Menu {

    private Context context;
    private Resources resources;

    private List<SimpleMenuItem> items = new ArrayList<SimpleMenuItem>();

    public SimpleMenu( Context context ){
        this.context = context;
        resources = context.getResources();
    }


    public Context getContext() {
        return context;
    }

    public Resources getResources() {
        return resources;
    }


    private MenuItem addInternal( int itemId,int order,CharSequence title ){
        final SimpleMenuItem item = new SimpleMenuItem(this,itemId,order,title );
         items.add( findInsertIndex(items,order),item );
        return item;
    }


    private int findInsertIndex(List<? extends MenuItem> items,int order ){
        for( int i=items.size()-1; i>=0; --i ){
            MenuItem item = items.get(i);
            if( item.getOrder() > order )
                continue;
            return i+1;
        }
        return 0;
    }


    private int findItemIndex(int id){
        final int size = size();
        for( int i=0;i<size;++i ){
            SimpleMenuItem item = items.get(i);
            if( item.getItemId() == id )
                return i;
        }
        return -1;
    }

    private MenuItem findMenuItem( int id ){
        int index = findItemIndex(id);
        if( -1 != index )
            return items.get(index);
        return null;
    }


    private void removeItemAtInt(int index){
        if( 0>index || size()<=index )
            return;
        items.remove(index);
    }


    @Override
    public MenuItem add(CharSequence sequence) {
        return addInternal(0,0,sequence);
    }

    @Override
    public MenuItem add(int titleRes) {
        return addInternal(0,0,resources.getString(titleRes));
    }

    @Override
    public MenuItem add(int groupId, int itemId, int order, CharSequence sequence) {
        return addInternal(itemId,order,sequence);
    }

    @Override
    public MenuItem add(int groupId, int itemId, int order,  int titleRes) {
        return addInternal( itemId,order, resources.getString(titleRes));
    }


    @Override
    public SubMenu addSubMenu(CharSequence sequence) {
        return null;
    }

    @Override
    public SubMenu addSubMenu(int i) {
        return null;
    }

    @Override
    public SubMenu addSubMenu(int i, int i2, int i3, CharSequence sequence) {
        return null;
    }

    @Override
    public SubMenu addSubMenu(int i, int i2, int i3, int i4) {
        return null;
    }

    @Override
    public int addIntentOptions(int i, int i2, int i3, ComponentName componentName, Intent[] intents, Intent intent, int i4, MenuItem[] menuItems) {
        return 0;
    }

    @Override
    public void removeItem(int itemId) {
        removeItemAtInt( findItemIndex(itemId) );
    }

    @Override
    public void removeGroup(int i) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void setGroupCheckable(int i, boolean b, boolean b2) {

    }

    @Override
    public void setGroupVisible(int i, boolean b) {

    }

    @Override
    public void setGroupEnabled(int i, boolean b) {

    }

    @Override
    public boolean hasVisibleItems() {
        return false;
    }

    @Override
    public MenuItem findItem(int i) {
        return items.get( findItemIndex(i));
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public MenuItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public void close() {

    }

    @Override
    public boolean performShortcut(int i, KeyEvent keyEvent, int i2) {
        return false;
    }

    @Override
    public boolean isShortcutKey(int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public boolean performIdentifierAction(int i, int i2) {
        return false;
    }

    @Override
    public void setQwertyMode(boolean b) {

    }
}
