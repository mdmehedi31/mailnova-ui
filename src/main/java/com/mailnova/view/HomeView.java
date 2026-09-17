package com.mailnova.view;


import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.router.Route;

@Route("")
public class HomeView extends Div {

    public HomeView() {
        initView();
    }

    private void initView() {

        NativeLabel lbl = new NativeLabel("This is Home view");
        this.add(lbl);
    }

}
