/*
 * Copyright (c) 2016, Alex Blewitt, Bandlem Ltd
 * Copyright (c) 2016, Packt Publishing Ltd
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.packtpub.e4.migration.views;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class SampleView {

	@Inject
	private MPart part;

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.packtpub.e4.migration.views.SampleView";

	private TableViewer viewer;

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	@PostConstruct
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setInput(new String[] { "One", "Two", "Three" });
		viewer.setLabelProvider(new ViewLabelProvider());

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.packtpub.e4.migration.viewer");
		hookDoubleClickAction();
		createToolBar();
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				new DoubleClick().run((IStructuredSelection) event.getSelection());
			}
		});
	}

	public static void showMessage(String message) {
		MessageDialog.openInformation(null, "Sample View", message);
	}

	@Focus
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private void createToolBar() {
		MDirectToolItem one = MMenuFactory.INSTANCE.createDirectToolItem();
		one.setTooltip("Action 1 tooltip");
		one.setIconURI("platform:/plugin/org.eclipse.ui/icons/full/obj16/info_tsk.png");
		one.setContributionURI("bundleclass://com.packtpub.e4.migration/com.packtpub.e4.migration.views.HandlerOne");
		MDirectToolItem two = MMenuFactory.INSTANCE.createDirectToolItem();
		two.setTooltip("Action 2 tooltip");
		two.setIconURI("platform:/plugin/org.eclipse.ui/icons/full/obj16/info_tsk.png");
		two.setContributionURI("bundleclass://com.packtpub.e4.migration/com.packtpub.e4.migration.views.HandlerTwo");
		MToolBar toolBar = MMenuFactory.INSTANCE.createToolBar();
		List<MToolBarElement> children = toolBar.getChildren();
		children.add(one);
		children.add(two);
		part.setToolbar(toolBar);
	}
}
