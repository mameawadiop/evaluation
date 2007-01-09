/******************************************************************************
 * ControlProducer.java - rewrite by aaronz@vt.edu
 * 
 * Copyright (c) 2007 Virginia Polytechnic Institute and State University
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 * Contributors:
 * Aaron Zeckoski (aaronz@vt.edu)
 * Rui Feng (fengr@vt.edu)
 *****************************************************************************/

package org.sakaiproject.evaluation.tool.producers;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.sakaiproject.evaluation.logic.EvalExternalLogic;
import org.sakaiproject.evaluation.logic.EvaluationLogic;
import org.sakaiproject.evaluation.model.EvalEvaluation;
import org.sakaiproject.evaluation.model.EvalTemplate;
import org.sakaiproject.evaluation.tool.params.EvalViewParameters;
import org.sakaiproject.evaluation.tool.params.PreviewEvalParameters;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/**
 * Rewritten to eliminate need for backing bean and refactored
 * 
 * @author Aaron Zeckoski (aaronz@vt.edu)
 * @author Rui Feng (fengr@vt.edu)
 */

public class ControlPanelProducer implements ViewComponentProducer, NavigationCaseReporter {

	public static final String VIEW_ID = "control_panel"; //$NON-NLS-1$
	public String getViewID() {
		return VIEW_ID;
	}

	private EvaluationLogic logic;
	public void setLogic(EvaluationLogic logic) {
		this.logic = logic;
	}

	private EvalExternalLogic external;
	public void setExternal(EvalExternalLogic external) {
		this.external = external;
	}

	private MessageLocator messageLocator;
	public void setMessageLocator(MessageLocator messageLocator) {
		this.messageLocator = messageLocator;
	}

	private Locale locale;
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {

		// local variables used in the render logic
		String currentUserId = external.getCurrentUserId();
		boolean userAdmin = external.isUserAdmin(currentUserId);
		boolean createTemplate = logic.canCreateTemplate(currentUserId);
		boolean beginEvaluation = logic.canBeginEvaluation(currentUserId);
		// use a date which is related to the current users locale
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);

		UIOutput.make(tofill, "control-panel-title", messageLocator.getMessage("controlpanel.page.title")); //$NON-NLS-1$ //$NON-NLS-2$

		/*
		 * top links here
		 */
		if (userAdmin) {
			UIInternalLink.make(tofill, "administrate-toplink", messageLocator.getMessage("administrate.page.title"), new SimpleViewParameters(AdministrateProducer.VIEW_ID)); //$NON-NLS-1$ //$NON-NLS-2$
		}

		UIInternalLink.make(tofill, "summary-toplink", messageLocator.getMessage("summary.page.title"), new SimpleViewParameters(SummaryProducer.VIEW_ID)); //$NON-NLS-1$ //$NON-NLS-2$

		// Create new template link
		if (createTemplate) {
			UIForm createTemplateForm = UIForm.make(tofill, "create-template-form"); //$NON-NLS-1$
			UICommand.make(createTemplateForm, "createTemplateLink", messageLocator.getMessage("controlpanel.create.template.link"), //$NON-NLS-1$ //$NON-NLS-2$
									"#{templateBean.createNewTemplate}"); //$NON-NLS-1$
			UIOutput.make(createTemplateForm, "eval-templates-header", messageLocator.getMessage("controlpanel.eval.templates.header")); //$NON-NLS-1$ //$NON-NLS-2$
			/*UIInternalLink.make(createTemplateForm, "createTemplateLink", new EvalViewParameters(TemplateProducer.VIEW_ID, null,
				ControlPanelProducer.VIEW_ID));*/
			UIOutput.make(createTemplateForm, "template-desc-note", messageLocator.getMessage("controlpanel.template.desc.note")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// get template List
		List templateList = logic.getTemplatesToDisplay(currentUserId);
		if (templateList != null && templateList.size() > 0) {

			UIBranchContainer templates = UIBranchContainer.make(tofill, "templateTable:"); //$NON-NLS-1$

			UIForm templateForm = UIForm.make(templates, "template-form"); //$NON-NLS-1$

			UIOutput.make(templateForm, "template-title-header", messageLocator.getMessage("controlpanel.template.title.header"));	 //$NON-NLS-1$ //$NON-NLS-2$
			UIOutput.make(templateForm, "owner-header", messageLocator.getMessage("controlpanel.owner.header")); //$NON-NLS-1$ //$NON-NLS-2$
			UIOutput.make(templateForm, "last-update-header", messageLocator.getMessage("controlpanel.last.update.header")); //$NON-NLS-1$ //$NON-NLS-2$
			
			for (int i = 0; i < templateList.size(); i++) {
				UIBranchContainer templatesRb = UIBranchContainer.make(templateForm, "templateList:", Integer //$NON-NLS-1$
						.toString(i));

				EvalTemplate template1 = (EvalTemplate) (templateList.get(i));

				UICommand templateUIC = UICommand.make(templatesRb, "templateTitleButton", template1.getTitle(), //$NON-NLS-1$
										"#{templateBean.modifyExistingTemplate}"); //$NON-NLS-1$
				templateUIC.parameters.add(new UIELBinding("#{templateBean.templateId}", template1.getId())); //$NON-NLS-1$
				
				String ownerId = template1.getOwner();
				UIOutput.make(templatesRb, "templateOwner", external.getUserDisplayName(ownerId)); //$NON-NLS-1$
				
				Date date1 = template1.getLastModified();
				UIOutput.make(templatesRb, "lastUpdate", df.format(date1)); //$NON-NLS-1$
				
				UIInternalLink.make(templatesRb, "deleteTemplateLink", new EvalViewParameters( //$NON-NLS-1$
						RemoveTemplateProducer.VIEW_ID,template1.getId(), ControlPanelProducer.VIEW_ID));

			}// end of for loop
		}


		// Begin eval link
		if (beginEvaluation) {
			UIForm startEvalForm = UIForm.make(tofill, "start-evaluation-form"); //$NON-NLS-1$
			UICommand.make(startEvalForm, "startEvalLink", messageLocator.getMessage("starteval.page.title"), //$NON-NLS-1$ //$NON-NLS-2$
									"#{evaluationBean.startEvaluation}"); //$NON-NLS-1$
			/*UIInternalLink.make(tofill, "startEvalLink", new EvalViewParameters(EvaluationStartProducer.VIEW_ID, null,
				ControlPanelProducer.VIEW_ID));*/
			UIOutput.make(startEvalForm, "queued-eval-header", messageLocator.getMessage("controlpanel.queued.eval.header")); //$NON-NLS-1$ //$NON-NLS-2$
			UIOutput.make(startEvalForm, "evals-not-started-header", messageLocator.getMessage("controlpanel.evals.not.started.header")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// get all the visible evaluation to current user
		List evals = logic.getVisibleEvaluationsForUser(external.getCurrentUserId(), true);
		if (evals != null && evals.size() > 0) {
			// get queued, active, closed evaluations by date
			List queuedEvals = new ArrayList();
			List activeEvals = new ArrayList();
			List closedEvals = new ArrayList();
			/* 
			 * if due day is the same date as current date, it should be considered active
			 */
			Calendar calendar = new GregorianCalendar();			
			Date today = new Date();
		
			for (int j = 0; j < evals.size(); j++) {
				EvalEvaluation myEval = (EvalEvaluation) evals.get(j);
				Date dueDate = myEval.getDueDate();
 				calendar.setTime(dueDate);
 				calendar.add(Calendar.DATE, +1);
 				Date dummyDueDate = calendar.getTime();
				if (today.before(myEval.getStartDate())) {
					queuedEvals.add(myEval);
				} else if (today.after(dummyDueDate)) {
					closedEvals.add(myEval);
				} else {
					activeEvals.add(myEval);
				}
			}

			// display Queued Evaluation Table
			if (queuedEvals != null && queuedEvals.size() > 0) {
				
				UIBranchContainer queuedEvalTable = UIBranchContainer.make(tofill, "queuedEvalTable:"); //$NON-NLS-1$
				UIForm queuedEvalForm = UIForm.make(queuedEvalTable, "queuedEvalForm"); //$NON-NLS-1$

				UIOutput.make(queuedEvalForm, "eval-title-header", messageLocator.getMessage("controlpanel.eval.title.header")); //$NON-NLS-1$ //$NON-NLS-2$
				UIOutput.make(queuedEvalForm, "eval-assigned-header", messageLocator.getMessage("controlpanel.eval.assigned.header")); //$NON-NLS-1$ //$NON-NLS-2$
				UIOutput.make(queuedEvalForm, "eval-start-date-header", messageLocator.getMessage("controlpanel.eval.start.date.header")); //$NON-NLS-1$ //$NON-NLS-2$
				UIOutput.make(queuedEvalForm, "eval-due-date-header", messageLocator.getMessage("controlpanel.eval.due.date.header")); //$NON-NLS-1$ //$NON-NLS-2$
				UIOutput.make(queuedEvalForm, "eval-settings-header", messageLocator.getMessage("controlpanel.eval.settings.header")); //$NON-NLS-1$ //$NON-NLS-2$
				
				
				for (int i = 0; i < queuedEvals.size(); i++) {
					UIBranchContainer queuedEvalsRb = UIBranchContainer.make(queuedEvalForm, "queuedEvalList:", //$NON-NLS-1$
							Integer.toString(i));

					EvalEvaluation eval1 = (EvalEvaluation) (queuedEvals.get(i));
/*
					UIInternalLink.make(queuedEvalsRb, "queuedEvalTitle", eval1.getTitle(), new EvalViewParameters(
									PreviewEvalProducer.VIEW_ID, eval1.getId(), ControlPanelProducer.VIEW_ID));
	*/			
					UIInternalLink.make(queuedEvalsRb, "queuedEvalTitle", eval1.getTitle(), new PreviewEvalParameters( //$NON-NLS-1$
							PreviewEvalProducer.VIEW_ID, eval1.getId(),eval1.getTemplate().getId(), null, ControlPanelProducer.VIEW_ID));
					
					int count = logic.countEvaluationContexts(eval1.getId());
					if (count > 1){
					
						UICommand queuedEvalAssigned = UICommand.make(queuedEvalsRb, "queuedEvalAssigned", count+"courses", //$NON-NLS-1$ //$NON-NLS-2$
														"#{evaluationBean.evalAssigned}"); //$NON-NLS-1$
						queuedEvalAssigned.parameters.add(new UIELBinding("#{evaluationBean.eval.id}", eval1.getId())); //$NON-NLS-1$
					}else{
						
						UICommand queuedEvalAssigned = UICommand
								.make(
										queuedEvalsRb,
										"queuedEvalAssigned", logic.getCourseTitle(eval1.getId()), //$NON-NLS-1$
														"#{evaluationBean.evalAssigned}"); //$NON-NLS-1$
						queuedEvalAssigned.parameters.add(new UIELBinding("#{evaluationBean.eval.id}", eval1.getId())); //$NON-NLS-1$
					}

					UIOutput.make(queuedEvalsRb, "queuedEvalStartDate", df.format(eval1.getStartDate())); //$NON-NLS-1$
					UIOutput.make(queuedEvalsRb, "queuedEvalDueDate", df.format(eval1.getDueDate())); //$NON-NLS-1$
					
					UICommand queuedEvalEdit = UICommand.make(queuedEvalsRb, "queuedEvalEditLink", messageLocator.getMessage("controlpanel.eval.edit.link"), //$NON-NLS-1$ //$NON-NLS-2$
												"#{evaluationBean.editEvalSettingAction}"); //$NON-NLS-1$
					queuedEvalEdit.parameters.add(new UIELBinding("#{evaluationBean.eval.id}", eval1.getId())); //$NON-NLS-1$
					
					//Use EvalViewParameter to pass Evaluatio ID
					UIInternalLink.make(queuedEvalsRb, "deleteEvalLink", new EvalViewParameters( //$NON-NLS-1$
							RemoveEvalProducer.VIEW_ID, eval1.getId(), ControlPanelProducer.VIEW_ID));

				} // end of for loop
			}

			UIOutput.make(tofill, "active-eval-header-title", messageLocator.getMessage("controlpanel.active.eval.header.title")); //$NON-NLS-1$ //$NON-NLS-2$
			UIOutput.make(tofill, "active-eval-header-desc", messageLocator.getMessage("controlpanel.active.eval.header.desc")); //$NON-NLS-1$ //$NON-NLS-2$
			
			// display Active Evaluation Table
			if (activeEvals != null && activeEvals.size() > 0) {
				
				UIBranchContainer activeEvalTable = UIBranchContainer.make(tofill, "activeEvalTable:"); //$NON-NLS-1$
				UIForm activeEvalForm = UIForm.make(activeEvalTable, "activeEvalForm"); //$NON-NLS-1$
				
				UIOutput.make(activeEvalForm, "eval-title-header", messageLocator.getMessage("controlpanel.eval.title.header")); //$NON-NLS-1$ //$NON-NLS-2$
				UIOutput.make(activeEvalForm, "eval-assigned-header", messageLocator.getMessage("controlpanel.eval.assigned.header")); //$NON-NLS-1$ //$NON-NLS-2$
				UIOutput.make(activeEvalForm, "eval-start-date-header", messageLocator.getMessage("controlpanel.eval.start.date.header")); //$NON-NLS-1$ //$NON-NLS-2$
				UIOutput.make(activeEvalForm, "eval-due-date-header", messageLocator.getMessage("controlpanel.eval.due.date.header")); //$NON-NLS-1$ //$NON-NLS-2$
				UIOutput.make(activeEvalForm, "eval-users-header", messageLocator.getMessage("controlpanel.eval.users.header"));				 //$NON-NLS-1$ //$NON-NLS-2$
				UIOutput.make(activeEvalForm, "eval-settings-header", messageLocator.getMessage("controlpanel.eval.settings.header")); //$NON-NLS-1$ //$NON-NLS-2$
				
				for (int i = 0; i < activeEvals.size(); i++) {
					UIBranchContainer activeEvalsRb = UIBranchContainer.make(activeEvalForm, "activeEvalList:", //$NON-NLS-1$
							Integer.toString(i));

					EvalEvaluation eval1 = (EvalEvaluation) (activeEvals.get(i));
/*
					UIInternalLink.make(activeEvalsRb, "activeEvalTitle", eval1.getTitle(), new EvalViewParameters(
										PreviewEvalProducer.VIEW_ID, eval1.getId(), ControlPanelProducer.VIEW_ID));
*/	
					UIInternalLink.make(activeEvalsRb, "activeEvalTitle", eval1.getTitle(),new PreviewEvalParameters( //$NON-NLS-1$
							PreviewEvalProducer.VIEW_ID, eval1.getId(),eval1.getTemplate().getId(), null, ControlPanelProducer.VIEW_ID));
					
					int count = logic.countEvaluationContexts(eval1.getId());
					if (count > 1){
					
						UICommand activeEvalAssigned = UICommand.make(activeEvalsRb, "activeEvalAssigned", count+"courses", //$NON-NLS-1$ //$NON-NLS-2$
										"#{evaluationBean.evalAssigned}"); //$NON-NLS-1$
						activeEvalAssigned.parameters.add(new UIELBinding("#{evaluationBean.eval.id}", eval1.getId())); //$NON-NLS-1$
					}else{
				
						UICommand activeEvalAssigned = UICommand.make(activeEvalsRb, "activeEvalAssigned", logic.getCourseTitle(eval1.getId()), //$NON-NLS-1$
										"#{evaluationBean.evalAssigned}"); //$NON-NLS-1$
						activeEvalAssigned.parameters.add(new UIELBinding("#{evaluationBean.eval.id}", eval1.getId())); //$NON-NLS-1$
					}

					UIOutput.make(activeEvalsRb, "activeEvalStartDate", df.format(eval1.getStartDate())); //$NON-NLS-1$
					UIOutput.make(activeEvalsRb, "activeEvalDueDate", df.format(eval1.getDueDate())); //$NON-NLS-1$
					
					UICommand activeEvalEdit = UICommand.make(activeEvalsRb, "activeEvalEditLink", messageLocator.getMessage("controlpanel.eval.edit.link"), //$NON-NLS-1$ //$NON-NLS-2$
											"#{evaluationBean.editEvalSettingAction}"); //$NON-NLS-1$
					activeEvalEdit.parameters.add(new UIELBinding("#{evaluationBean.eval.id}", eval1.getId())); //$NON-NLS-1$
					
					//GET user Rate: Response/Enrollement
					UIOutput.make(activeEvalsRb, "users", logic.countResponses(eval1.getId(), null)+"/"  //$NON-NLS-1$ //$NON-NLS-2$
													+ logic.getTotalEnrollments(eval1.getId()));
				} // end of for loop
			}

			UIOutput.make(tofill, "eval-closed-header-title", messageLocator.getMessage("controlpanel.eval.closed.header.title")); //$NON-NLS-1$ //$NON-NLS-2$
			UIOutput.make(tofill, "eval-closed-header-desc", messageLocator.getMessage("controlpanel.eval.closed.header.desc")); //$NON-NLS-1$ //$NON-NLS-2$

			// display closed Evaluation Table
			if (closedEvals != null && closedEvals.size() > 0) {
				UIBranchContainer closedEvalTable = UIBranchContainer.make(tofill, "closedEvalTable:"); //$NON-NLS-1$

				UIOutput.make(closedEvalTable, "eval-title-header", messageLocator.getMessage("controlpanel.eval.title.header")); //$NON-NLS-1$ //$NON-NLS-2$
				UIOutput.make(closedEvalTable, "eval-due-date-header", messageLocator.getMessage("controlpanel.eval.due.date.header")); //$NON-NLS-1$ //$NON-NLS-2$
				UIOutput.make(closedEvalTable, "eval-response-rate-header", messageLocator.getMessage("controlpanel.eval.response.rate.header"));				 //$NON-NLS-1$ //$NON-NLS-2$
				UIOutput.make(closedEvalTable, "eval-settings-header", messageLocator.getMessage("controlpanel.eval.settings.header")); //$NON-NLS-1$ //$NON-NLS-2$

				
				for (int i = 0; i < closedEvals.size(); i++) {
					UIBranchContainer closedEvalsRb = UIBranchContainer.make(closedEvalTable, "closedEvalList:", //$NON-NLS-1$
							Integer.toString(i));

					EvalEvaluation eval1 = (EvalEvaluation) (closedEvals.get(i));
/*
					UIInternalLink.make(closedEvalsRb, "closedEvalTitle", eval1.getTitle(), new EvalViewParameters(
							PreviewEvalProducer.VIEW_ID, eval1.getId(), ControlPanelProducer.VIEW_ID));
*/
					UIInternalLink.make(closedEvalsRb, "closedEvalTitle", eval1.getTitle(),new PreviewEvalParameters( //$NON-NLS-1$
							PreviewEvalProducer.VIEW_ID, eval1.getId(),eval1.getTemplate().getId(), null, ControlPanelProducer.VIEW_ID));
					
					UIOutput.make(closedEvalsRb, "closedEvalDueDate", df.format(eval1.getDueDate())); //$NON-NLS-1$
					// get response, get enrollment
					int ctResponses = logic.countResponses(eval1.getId(), null);
					int ctEnrollments =logic.getTotalEnrollments(eval1.getId());
					double per = (1.0 * ctResponses )/ctEnrollments;
					long percentage = Math.round(per*100);
					
					UIOutput.make(closedEvalsRb, "reponseRate", ctResponses + "/"+ ctEnrollments +" - "+percentage +"%"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					UIInternalLink.make(closedEvalsRb, "viewReportLink", messageLocator.getMessage("controlpanel.eval.report.link"),  //$NON-NLS-1$ //$NON-NLS-2$
							new EvalViewParameters(ViewReportProducer.VIEW_ID, 
								eval1.getId(), ControlPanelProducer.VIEW_ID));	
				} // end of for loop
			}

		} // end of :if (evals !=null && evals.size() > 0)

	} // end of medthod:fillComponents

	public List reportNavigationCases() {
		List i = new ArrayList();
		
		i.add(new NavigationCase(TemplateModifyProducer.VIEW_ID, new SimpleViewParameters(
				TemplateModifyProducer.VIEW_ID)));
		i.add(new NavigationCase(TemplateProducer.VIEW_ID, new SimpleViewParameters(
				TemplateProducer.VIEW_ID)));
		i.add(new NavigationCase(EvaluationSettingsProducer.VIEW_ID, new SimpleViewParameters(
				EvaluationSettingsProducer.VIEW_ID)));
		i.add(new NavigationCase(EvaluationStartProducer.VIEW_ID, new SimpleViewParameters(
				EvaluationStartProducer.VIEW_ID)));
		i.add(new NavigationCase(EvaluationAssignConfirmProducer.VIEW_ID, new SimpleViewParameters(
				EvaluationAssignConfirmProducer.VIEW_ID)));
		
		return i;
	}

}
