package com.github.hanzm_10.murico.swingapp.scenes.home;

import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.ui.components.reports.ReportsHeader;
import com.github.hanzm_10.murico.swingapp.ui.components.reports.ReportsTable;

import net.miginfocom.swing.MigLayout;

public class ReportsScene implements Scene {
	private JPanel view;

    private ReportsHeader header;
    private ReportsTable table;

	@Override
	public String getSceneName() {
		return "reports";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public void onCreate() {
        view.setLayout(new MigLayout("", "[grow, left]", "[96px::96px,grow][grow]"));

        header = new ReportsHeader();
	    table = new ReportsTable();

        view.add(header.getContainer(), "cell 0 0, grow");
        view.add(table.getContainer(), "cell 0 1, grow");
    }

    @Override
    public boolean onDestroy() {
        header.destroy();
        table.destroy();

        return true;
    }
}
