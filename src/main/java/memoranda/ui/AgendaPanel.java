package memoranda.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.table.DefaultTableModel;

import memoranda.CurrentProject;
import memoranda.EventNotificationListener;
import memoranda.EventsManager;
import memoranda.EventsScheduler;
import memoranda.History;
import memoranda.NoteList;
import memoranda.Project;
import memoranda.ProjectListener;
import memoranda.ProjectManager;
import memoranda.ResourcesList;
import memoranda.TaskList;
import memoranda.date.CalendarDate;
import memoranda.date.CurrentDate;
import memoranda.date.DateListener;
import memoranda.util.AgendaGenerator;
import memoranda.util.CurrentStorage;
import memoranda.util.Local;
import memoranda.util.Util;

import nu.xom.Element;

// New code, for US4-UI
import memoranda.ui.DailyItemsPanel;
import memoranda.ui.ExceptionDialog;

/*$Id: AgendaPanel.java,v 1.11 2005/02/15 16:58:02 rawsushi Exp $*/
public class AgendaPanel extends JPanel {
	BorderLayout borderLayout1 = new BorderLayout();
	JButton historyBackB = new JButton();
	JToolBar toolBar = new JToolBar();
	JButton historyForwardB = new JButton();
	JButton export = new JButton();
	JEditorPane viewer = new JEditorPane("text/html", "");
	String[] priorities = {"Very Tall","Tall","Medium","Short","Very Short"};
	JScrollPane scrollPane = new JScrollPane();

	DailyItemsPanel parentPanel = null;

	//	JPopupMenu agendaPPMenu = new JPopupMenu();
	//	JCheckBoxMenuItem ppShowActiveOnlyChB = new JCheckBoxMenuItem();

	Collection expandedTasks;
	String gotoTask = null;

	boolean isActive = true;

	public AgendaPanel(DailyItemsPanel _parentPanel) {
		try {
			// parentPanel = _parentPanel;
			jbInit();
		} catch (Exception ex) {
			new ExceptionDialog(ex);
			ex.printStackTrace();
		}
	}
	void jbInit() throws Exception {
		/* Old Code, some may be necessary or needed later, to be deleted at a later point
		expandedTasks = new ArrayList();

		toolBar.setFloatable(false);
		viewer.setEditable(false);
		viewer.setOpaque(false);
		viewer.addHyperlinkListener(new HyperlinkListener() {

			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					String d = e.getDescription();
					if (d.equalsIgnoreCase("memoranda:events"))
						parentPanel.alarmB_actionPerformed(null);
					else if (d.startsWith("memoranda:tasks")) {
						String id = d.split("#")[1];
						CurrentProject.set(Objects.requireNonNull(ProjectManager.getProject(id)));
						parentPanel.taskB_actionPerformed(null);
					} else if (d.startsWith("memoranda:project")) {
						String id = d.split("#")[1];
						CurrentProject.set(Objects.requireNonNull(ProjectManager.getProject(id)));
					} else if (d.startsWith("memoranda:removesticker")) {
                        String id = d.split("#")[1];
                        StickerConfirmation stc = new StickerConfirmation(App.getFrame());
                        Dimension frmSize = App.getFrame().getSize();
                        stc.setSize(new Dimension(300,180));
                        Point loc = App.getFrame().getLocation();
                        stc.setLocation(
                                (frmSize.width - stc.getSize().width) / 2 + loc.x,
                                (frmSize.height - stc.getSize().height) / 2
                                        + loc.y);
                        stc.setVisible(true);
                        if (!stc.CANCELLED) {
                        EventsManager.removeSticker(id);
                        CurrentStorage.get().storeEventsManager();}
                        refresh(CurrentDate.get());
					} else if (d.startsWith("memoranda:addsticker")) {
						StickerDialog dlg = new StickerDialog(App.getFrame());
						Dimension frmSize = App.getFrame().getSize();
						dlg.setSize(new Dimension(300,380));
						Point loc = App.getFrame().getLocation();
						dlg.setLocation(
								(frmSize.width - dlg.getSize().width) / 2 + loc.x,
								(frmSize.height - dlg.getSize().height) / 2
								+ loc.y);
						dlg.setVisible(true);
						if (!dlg.CANCELLED) {
							String txt = dlg.getStickerText();
							int sP = dlg.getPriority();
							txt = txt.replaceAll("\\n", "<br>");
                            txt = "<div style=\"background-color:"+dlg.getStickerColor()+";font-size:"+dlg.getStickerTextSize()+";color:"+dlg.getStickerTextColor()+"; \">"+txt+"</div>";
							EventsManager.createSticker(txt, sP);
							CurrentStorage.get().storeEventsManager();
						}
						refresh(CurrentDate.get());
						System.out.println("add a sticker");
					} else if (d.startsWith("memoranda:expandsubtasks")) {
						String id = d.split("#")[1];
						gotoTask = id;
						expandedTasks.add(id);
						refresh(CurrentDate.get());
					} else if (d.startsWith("memoranda:closesubtasks")) {
						String id = d.split("#")[1];
						gotoTask = id;
						expandedTasks.remove(id);
						refresh(CurrentDate.get());
					} else if (d.startsWith("memoranda:expandsticker")) {
						String id = d.split("#")[1];
						Element pre_sticker=(Element)((Map)EventsManager.getStickers()).get(id);
						String sticker = pre_sticker.getValue();
						int first=sticker.indexOf(">");
						int last=sticker.lastIndexOf("<");
						int backcolor=sticker.indexOf("#");
						int fontcolor=sticker.indexOf("#", backcolor+1);
						int sP=Integer.parseInt(pre_sticker.getAttributeValue("priority"));
						String backGroundColor=sticker.substring(backcolor, sticker.indexOf(';',backcolor));
						String foreGroundColor=sticker.substring(fontcolor, sticker.indexOf(';',fontcolor));
						sticker="<html>"+sticker.substring(first+1, last)+"</html>";
						StickerExpand dlg = new StickerExpand(App.getFrame(),sticker,backGroundColor,foreGroundColor,Local.getString("priority")+": "+Local.getString(priorities[sP]));
						Dimension frmSize = App.getFrame().getSize();
						dlg.setSize(new Dimension(300,200));
						Point loc = App.getFrame().getLocation();
						dlg.setLocation(
								(frmSize.width - dlg.getSize().width) / 2 + loc.x,
								(frmSize.height - dlg.getSize().height) / 2
								+ loc.y);
						dlg.stickerText.setText(sticker);
						dlg.setVisible(true);
					}else if (d.startsWith("memoranda:editsticker")) {
						String id = d.split("#")[1];
						Element pre_sticker=(Element)((Map)EventsManager.getStickers()).get(id);
						String sticker = pre_sticker.getValue();
						sticker=sticker.replaceAll("<br>","\n");
						int first=sticker.indexOf(">");
						int last=sticker.lastIndexOf("<");
						int backcolor=sticker.indexOf("#");
						int fontcolor=sticker.indexOf("#", backcolor+1);
						int sizeposition=sticker.indexOf("font-size")+10;
						int size=Integer.parseInt(sticker.substring(sizeposition,sizeposition+2));
						System.out.println(size+" "+sizeposition);
						int sP=Integer.parseInt(pre_sticker.getAttributeValue("priority"));
						String backGroundColor=sticker.substring(backcolor, sticker.indexOf(';',backcolor));
						String foreGroundColor=sticker.substring(fontcolor, sticker.indexOf(';',fontcolor));
						StickerDialog dlg = new StickerDialog(App.getFrame(), sticker.substring(first+1, last), backGroundColor, foreGroundColor, sP, size);
						Dimension frmSize = App.getFrame().getSize();
						dlg.setSize(new Dimension(300,380));
						Point loc = App.getFrame().getLocation();
						dlg.setLocation((frmSize.width - dlg.getSize().width) / 2 + loc.x,
							 		(frmSize.height - dlg.getSize().height) / 2 + loc.y);
						dlg.setVisible(true);
						if (!dlg.CANCELLED) {
							String txt = dlg.getStickerText();
							sP = dlg.getPriority();
							txt = txt.replaceAll("\\n", "<br>");
							txt = "<div style=\"background-color:"+dlg.getStickerColor()+";font-size:"+dlg.getStickerTextSize()+";color:"+dlg.getStickerTextColor()+";\">"+txt+"</div>";
							EventsManager.removeSticker(id);
							EventsManager.createSticker(txt, sP);
							CurrentStorage.get().storeEventsManager();
						 }
						 refresh(CurrentDate.get());
					}else if (d.startsWith("memoranda:exportstickerst")) {
						 //  Falta agregar el exportar sticker mientras tanto..
						 final JFrame parent = new JFrame();
						 String name = JOptionPane.showInputDialog(parent,Local.getString("Enter name of file to export"),null);
						 new ExportSticker(name).export("txt");
						 //JOptionPane.showMessageDialog(null,name);
					}else if (d.startsWith("memoranda:exportstickersh")) {
						 //  Falta agregar el exportar sticker mientras tanto..
						 final JFrame parent = new JFrame();
						 String name = JOptionPane.showInputDialog(parent,Local.getString("Enter name of file to export"),null);
						 new ExportSticker(name).export("html");
						 //JOptionPane.showMessageDialog(null,name);
					}else if (d.startsWith("memoranda:importstickers")) {
						final JFrame parent = new JFrame();
						String name = JOptionPane.showInputDialog(parent,Local.getString("Enter name of file to import"),null);
						new ImportSticker(name).import_file();
					}
				}
			}
		});
		historyBackB.setAction(History.historyBackAction);
		historyBackB.setFocusable(false);
		historyBackB.setBorderPainted(false);
		historyBackB.setToolTipText(Local.getString("History back"));
		historyBackB.setRequestFocusEnabled(false);
		historyBackB.setPreferredSize(new Dimension(24, 24));
		historyBackB.setMinimumSize(new Dimension(24, 24));
		historyBackB.setMaximumSize(new Dimension(24, 24));
		historyBackB.setText("");

		historyForwardB.setAction(History.historyForwardAction);
		historyForwardB.setBorderPainted(false);
		historyForwardB.setFocusable(false);
		historyForwardB.setPreferredSize(new Dimension(24, 24));
		historyForwardB.setRequestFocusEnabled(false);
		historyForwardB.setToolTipText(Local.getString("History forward"));
		historyForwardB.setMinimumSize(new Dimension(24, 24));
		historyForwardB.setMaximumSize(new Dimension(24, 24));
		historyForwardB.setText("");

		this.setLayout(borderLayout1);
		scrollPane.getViewport().setBackground(Color.white);

		scrollPane.getViewport().add(viewer, null);
		this.add(scrollPane, BorderLayout.CENTER);
		toolBar.add(historyBackB, null);
		toolBar.add(historyForwardB, null);
		toolBar.addSeparator(new Dimension(8, 24));

		this.add(toolBar, BorderLayout.NORTH);

		CurrentDate.addDateListener(new DateListener() {
			public void dateChange(CalendarDate d) {
				if (isActive)
					refresh(d);
			}
		});
		CurrentProject.addProjectListener(new ProjectListener() {

			public void projectChange(
					Project prj,
					NoteList nl,
					TaskList tl,
					ResourcesList rl) {
			}

			public void projectWasChanged() {
				if (isActive)
					refresh(CurrentDate.get());
			}});
		EventsScheduler.addListener(new EventNotificationListener() {
			public void eventIsOccured(memoranda.Event ev) {
				if (isActive)
					refresh(CurrentDate.get());
			}

			public void eventsChanged() {
				if (isActive)
					refresh(CurrentDate.get());
			}
		});
		refresh(CurrentDate.get());

		//        agendaPPMenu.setFont(new java.awt.Font("Dialog", 1, 10));
		//        agendaPPMenu.add(ppShowActiveOnlyChB);
		//        PopupListener ppListener = new PopupListener();
		//        viewer.addMouseListener(ppListener);
		//		ppShowActiveOnlyChB.setFont(new java.awt.Font("Dialog", 1, 11));
		//		ppShowActiveOnlyChB.setText(
		//			Local.getString("Show Active only"));
		//		ppShowActiveOnlyChB.addActionListener(new java.awt.event.ActionListener() {
		//			public void actionPerformed(ActionEvent e) {
		//				toggleShowActiveOnly_actionPerformed(e);
		//			}
		//		});		
		//		boolean isShao =
		//			(Context.get("SHOW_ACTIVE_TASKS_ONLY") != null)
		//				&& (Context.get("SHOW_ACTIVE_TASKS_ONLY").equals("true"));
		//		ppShowActiveOnlyChB.setSelected(isShao);
		//		toggleShowActiveOnly_actionPerformed(null);
		*/

		// New code -- US 4 Agenda UI
		expandedTasks = new ArrayList();

		this.setLayout(new BorderLayout());

		// Top Panel: Course Information and Course Switching
		JPanel topPanel = new JPanel(new BorderLayout());
		JPanel courseInfoPanel = new JPanel(new GridLayout(2, 2, 10, 10));
		courseInfoPanel.setBorder(BorderFactory.createTitledBorder("Course Information"));
		courseInfoPanel.add(new JLabel("Start Date:"));
		courseInfoPanel.add(new JLabel("Placeholder for Start Date"));
		courseInfoPanel.add(new JLabel("End Date:"));
		courseInfoPanel.add(new JLabel("Placeholder for End Date"));
		courseInfoPanel.add(new JLabel("Holidays:"));
		JList<String> holidaysList = new JList<>(new String[]{"Holiday 1", "Holiday 2"});
		courseInfoPanel.add(new JScrollPane(holidaysList));
		courseInfoPanel.add(new JLabel("Lecture Times:"));
		courseInfoPanel.add(new JLabel("Placeholder for Lecture Times"));

		// Course Switching ComboBox
		JPanel courseSwitchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		courseSwitchPanel.add(new JLabel("Switch Course:"));
		JComboBox<String> courseSelector = new JComboBox<>(new String[]{"Course 1", "Course 2", "Course 3"});
		courseSwitchPanel.add(courseSelector);

		topPanel.add(courseInfoPanel, BorderLayout.CENTER);
		topPanel.add(courseSwitchPanel, BorderLayout.NORTH);

		// Middle Panel: Assignments, Topics, and TA/Grader Due Dates
		JPanel middlePanel = new JPanel(new GridLayout(1, 3, 10, 10));

		// Assignments Section
		JPanel assignmentsPanel = new JPanel(new BorderLayout());
		assignmentsPanel.setBorder(BorderFactory.createTitledBorder("Assignments"));
		String[] assignmentColumns = {"Title", "Due Date", "Day of Week", "Tags"};
		DefaultTableModel assignmentModel = new DefaultTableModel(assignmentColumns, 0);
		JTable assignmentsTable = new JTable(assignmentModel);
		assignmentModel.addRow(new Object[]{"Assignment 1", "2024-11-20", "Wednesday", "Sprint 1"});
		assignmentModel.addRow(new Object[]{"Assignment 2", "2024-11-27", "Wednesday", "Sprint 2"});
		assignmentsPanel.add(new JScrollPane(assignmentsTable), BorderLayout.CENTER);

		// Topics Section
		JPanel topicsPanel = new JPanel(new BorderLayout());
		topicsPanel.setBorder(BorderFactory.createTitledBorder("Lecture Topics"));
		String[] lectureTopics = {"Topic 1: Introduction", "Topic 2: Advanced Concepts"};
		JList<String> topicsList = new JList<>(lectureTopics);
		topicsPanel.add(new JScrollPane(topicsList), BorderLayout.CENTER);

		// TA/Grader Due Dates Section
		JPanel taGraderPanel = new JPanel(new BorderLayout());
		taGraderPanel.setBorder(BorderFactory.createTitledBorder("TA/Grader Due Dates"));
		String[] taGraderColumns = {"Task", "Due Date", "Assigned To", "Visible to Students"};
		DefaultTableModel taGraderModel = new DefaultTableModel(taGraderColumns, 0);
		JTable taGraderTable = new JTable(taGraderModel);
		taGraderModel.addRow(new Object[]{"Prepare Lecture Notes", "2024-11-18", "Grader Alice", "true"});
		taGraderModel.addRow(new Object[]{"Review Assignment 1", "2024-11-22", "TA Bob", "false"});
		taGraderPanel.add(new JScrollPane(taGraderTable), BorderLayout.CENTER);

		middlePanel.add(assignmentsPanel);
		middlePanel.add(topicsPanel);
		middlePanel.add(taGraderPanel);

		// Bottom Panel: Notes and Documents
		JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 10));

		// Notes Section
		JPanel notesPanel = new JPanel(new BorderLayout());
		notesPanel.setBorder(BorderFactory.createTitledBorder("Instructor Notes/Tasks"));
		JTextArea notesArea = new JTextArea("Placeholder for Notes/Tasks");
		notesPanel.add(new JScrollPane(notesArea), BorderLayout.CENTER);

		// Documents Section
		JPanel documentsPanel = new JPanel(new BorderLayout());
		documentsPanel.setBorder(BorderFactory.createTitledBorder("Documents and Links"));
		DefaultListModel<String> documentListModel = new DefaultListModel<>();
		documentListModel.addElement("Document 1: Syllabus.pdf");
		documentListModel.addElement("Document 2: Lecture Notes.docx");
		JList<String> documentsList = new JList<>(documentListModel);
		documentsPanel.add(new JScrollPane(documentsList), BorderLayout.CENTER);

		bottomPanel.add(notesPanel);
		bottomPanel.add(documentsPanel);

		// Add all sections to the AgendaPanel
		this.add(topPanel, BorderLayout.NORTH);
		this.add(middlePanel, BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);

		refresh(CurrentDate.get());
	}

	public void refresh(CalendarDate date) {
		viewer.setText(AgendaGenerator.getAgenda(date, expandedTasks));
		SwingUtilities.invokeLater(() -> {
			if (gotoTask != null) {
				viewer.scrollToReference(gotoTask);
				scrollPane.setViewportView(viewer);
			}
		});
	}

	public void setActive(boolean isa) {
		isActive = isa;
	}

	//	void toggleShowActiveOnly_actionPerformed(ActionEvent e) {
	//		Context.put(
	//			"SHOW_ACTIVE_TASKS_ONLY",
	//			new Boolean(ppShowActiveOnlyChB.isSelected()));
	//		/*if (taskTable.isShowActiveOnly()) {
	//			// is true, toggle to false
	//			taskTable.setShowActiveOnly(false);
	//			//showActiveOnly.setToolTipText(Local.getString("Show Active Only"));			
	//		}
	//		else {
	//			// is false, toggle to true
	//			taskTable.setShowActiveOnly(true);
	//			showActiveOnly.setToolTipText(Local.getString("Show All"));			
	//		}*/	    
	//		refresh(CurrentDate.get());
	////		parentPanel.updateIndicators();
	//		//taskTable.updateUI();
	//	}

	//    class PopupListener extends MouseAdapter {
	//
	//        public void mouseClicked(MouseEvent e) {
	//        	System.out.println("mouse clicked!");
	////			if ((e.getClickCount() == 2) && (taskTable.getSelectedRow() > -1))
	////				editTaskB_actionPerformed(null);
	//		}
	//
	//		public void mousePressed(MouseEvent e) {
	//        	System.out.println("mouse pressed!");
	//			maybeShowPopup(e);
	//		}
	//
	//		public void mouseReleased(MouseEvent e) {
	//        	System.out.println("mouse released!");
	//			maybeShowPopup(e);
	//		}
	//
	//		private void maybeShowPopup(MouseEvent e) {
	//			if (e.isPopupTrigger()) {
	//				agendaPPMenu.show(e.getComponent(), e.getX(), e.getY());
	//			}
	//		}
	//
	//    }
}
