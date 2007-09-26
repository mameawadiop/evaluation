/******************************************************************************
 * ExistingItemsProducer.java - created by aaronz on 12 Mar 2007
 * 
 * Copyright (c) 2007 Centre for Academic Research in Educational Technologies
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 * Contributors:
 * Aaron Zeckoski (aaronz@vt.edu) - primary
 * 
 *****************************************************************************/

package org.sakaiproject.evaluation.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.evaluation.logic.EvalExternalLogic;
import org.sakaiproject.evaluation.logic.EvalItemsLogic;
import org.sakaiproject.evaluation.model.EvalItem;
import org.sakaiproject.evaluation.tool.viewparams.ChooseItemViewParameters;
import org.sakaiproject.evaluation.tool.viewparams.TemplateViewParameters;

import uk.org.ponder.rsf.components.UIBoundBoolean;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.components.decorators.DecoratorList;
import uk.org.ponder.rsf.components.decorators.UILabelTargetDecorator;
import uk.org.ponder.rsf.components.decorators.UIStyleDecorator;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

/**
 * Handles the choose existing items view
 * 
 * @author Aaron Zeckoski (aaronz@vt.edu)
 */
public class ExistingItemsProducer implements ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter {

	/**
	 * Used for navigation within the system, this must match with the template name
	 */
	public static final String VIEW_ID = "choose_existing_items";
	public String getViewID() {
		return VIEW_ID;
	}

	// Spring injection
	private EvalExternalLogic external;
	public void setExternal(EvalExternalLogic external) {
		this.external = external;
	}

	private EvalItemsLogic itemsLogic;
	public void setItemsLogic(EvalItemsLogic itemsLogic) {
		this.itemsLogic = itemsLogic;
	}

	/* (non-Javadoc)
	 * @see uk.org.ponder.rsf.view.ComponentProducer#fillComponents(uk.org.ponder.rsf.components.UIContainer, uk.org.ponder.rsf.viewstate.ViewParameters, uk.org.ponder.rsf.view.ComponentChecker)
	 */
	public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {

		String currentUserId = external.getCurrentUserId();

		ChooseItemViewParameters itemViewParameters = (ChooseItemViewParameters) viewparams;
		Long templateId = itemViewParameters.templateId;
		String searchString = itemViewParameters.searchString;
		if (searchString == null) searchString = "";

		UIMessage.make(tofill, "page-title", "items.existing.page.title"); //$NON-NLS-2$

		UIInternalLink.make(tofill, "summary-toplink", UIMessage.make("summary.page.title"), //$NON-NLS-2$
				new SimpleViewParameters(SummaryProducer.VIEW_ID));
		UIInternalLink.make(tofill, "modify-items-link", UIMessage.make("items.page.title"), //$NON-NLS-2$
				new SimpleViewParameters(SummaryProducer.VIEW_ID));

		UIInternalLink.make(tofill, "modify-template-link", UIMessage.make("modifytemplate.page.title"), //$NON-NLS-2$
				new TemplateViewParameters(ModifyTemplateItemsProducer.VIEW_ID, templateId) );

		UIMessage.make(tofill, "items-choose-items", "items.choose.items"); //$NON-NLS-2$
		
		if (searchString.length() > 0) {
			UIMessage.make(tofill, "search-current", "existing.items");
			UIOutput.make(tofill, "search-current-value", searchString);
		}

		UIForm searchForm = UIForm.make(tofill, "search-form", itemViewParameters);
		UIMessage.make(searchForm, "search-command", "items.search.command" );
		UIInput.make(searchForm, "search-box", "#{searchString}");

		UIMessage.make(tofill, "item-select-col-title", "items.select.col.title"); //$NON-NLS-2$
		UIMessage.make(tofill, "item-item-col-title", "items.item.col.title"); //$NON-NLS-2$
		UIMessage.make(tofill, "item-list-summary", "item.list.summary"); //$NON-NLS-2$
		UIForm form = UIForm.make(tofill, "insert-items-form");
		
		// loop through all existing items
		List existingItems = itemsLogic.getItemsForUser(currentUserId, null, null, false);
		for (int i = 0; i < existingItems.size(); i++) {
			EvalItem item = (EvalItem) existingItems.get(i);
			UIBranchContainer items = UIBranchContainer.make(form, "item-list:", item.getId().toString());
			if (i % 2 == 0) {
				items.decorators = new DecoratorList( new UIStyleDecorator("itemsListOddLine") ); // must match the existing CSS class
			}

			UIBoundBoolean checkbox = UIBoundBoolean.make(items, "insert-item-checkbox", "#{expertItemsBean.selectedIds." + item.getId() + "}");
			UILabelTargetDecorator.targetLabel(UIOutput.make(items, "item-label"), checkbox);
			UIVerbatim.make(items, "item-text", item.getItemText());
			if (item.getScale() != null) {
				String scaleText = item.getScale().getTitle() + " (";
				for (int j = 0; j < item.getScale().getOptions().length; j++) {
					scaleText += (j==0?"":",") + item.getScale().getOptions()[j];
				}
				scaleText += ")";
				UIOutput.make(items, "item-scale", scaleText);
			} else {
				UIOutput.make(items, "item-scale", item.getClassification());
			}
			if (item.getDescription() != null) {
				UIOutput.make(items, "item-desc", item.getExpertDescription());
			}
		}

		// create the cancel link
		UIInternalLink.make(tofill, "cancel-items", UIMessage.make("items.cancel"), 
				new TemplateViewParameters(ModifyTemplateItemsProducer.VIEW_ID, templateId) );

		// create the Insert Items button
		UICommand addItemsCommand = UICommand.make(form, "insert-items-command", UIMessage.make("expert.items.insert"),
			"#{expertItemsBean.processActionAddItems}");
		addItemsCommand.parameters.add(new UIELBinding("#{expertItemsBean.templateId}", templateId));
	}

	/* 
	 * (non-Javadoc)
	 * @see uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter#reportNavigationCases()
	 */
	public List reportNavigationCases() {
		List i = new ArrayList();
		i.add(new NavigationCase("success", new TemplateViewParameters(ModifyTemplateItemsProducer.VIEW_ID, null) ) );
		return i;
	}

	/* (non-Javadoc)
	 * @see uk.org.ponder.rsf.viewstate.ViewParamsReporter#getViewParameters()
	 */
	public ViewParameters getViewParameters() {
		return new ChooseItemViewParameters();
	}

}
