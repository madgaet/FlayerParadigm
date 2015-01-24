package com.trollCorporation.project.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.DefaultCaret;

import com.trollCorporation.common.exceptions.ConnectionException;
import com.trollCorporation.common.model.Message;
import com.trollCorporation.common.utils.Observer;
import com.trollCorporation.project.controllers.ChatboxOperationsController;
import com.trollCorporation.project.controllers.ChatboxOperationsControllerImpl;
import com.trollCorporation.project.controllers.FriendsController;
import com.trollCorporation.project.controllers.FriendsControllerImpl;
import com.trollCorporation.project.ihm.actions.SendingMessageKeyListener;
import com.trollCorporation.project.ihm.objects.ChatUser;
import com.trollCorporation.project.utils.ImageLoader;

public class ChatView extends JPanel implements Observer {

	private static final long serialVersionUID = 7679035020047514901L;
	
	public static final int MIN_CHATVIEW_WIDTH = HomePage.MIN_PAGE_WIDTH/5 - 10;
	public static final int MIN_CHATVIEW_HEIGHT = HomePage.MIN_PAGE_HEIGHT - BannerView.BANNER_HEIGHT - 10;
	
	private static final int TITLE_HEIGHT = 60;
	private static final int ACTIONS_BUTTONS_HEIGHT = 55;
	//Approximatively 10 rows
	private static final int READ_AREA_HEIGHT = 180;
	
	private String username;
	
	private JScrollPane jspFriendList;
	private JPanel friendList;
	private JButton jbAddFriend;
	private JButton jbRemoveFriend;
	private String selectedUser;
	
	private Map<String, ChatUser> chatUsers = new HashMap<String, ChatUser>();
	
	private ChatboxOperationsController chatboxController;
	private FriendsController friendsController;
	private Dimension dimension;
	
	public ChatView(final Dimension viewDimension, final String username) throws ConnectionException {
		this.dimension = viewDimension;
		this.setLayout(new BorderLayout());
		this.username = username;
		this.setMinimumSize(new Dimension(MIN_CHATVIEW_WIDTH, MIN_CHATVIEW_HEIGHT));
		this.addComponentListener(new ResizeListener());
		this.chatboxController = ChatboxOperationsControllerImpl.getInstance();
		this.friendsController = FriendsControllerImpl.getInstance();
		chatboxController.addObserver(this);
		friendsController.addObserver(this);
		this.add(createChatView(), BorderLayout.CENTER);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.setBackground(Color.GRAY);
		this.updateSize(dimension.width);
	}
	
	public Box createChatView() {
		Box chatView = Box.createVerticalBox();
		chatView.setMinimumSize(new Dimension(MIN_CHATVIEW_WIDTH, MIN_CHATVIEW_HEIGHT));
		chatView.setMaximumSize(new Dimension(dimension.width-10, dimension.height - BannerView.BANNER_HEIGHT - 10));
		chatView.setSize(chatView.getMinimumSize());
		
		//The view title
		JPanel jpTitle = new JPanel();
		jpTitle.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		jpTitle.setMinimumSize(new Dimension(MIN_CHATVIEW_WIDTH-10, TITLE_HEIGHT));
		jpTitle.setMaximumSize(new Dimension(dimension.width-10, TITLE_HEIGHT));
		jpTitle.setSize(jpTitle.getMinimumSize());
		jpTitle.add(new JLabel("<html><h1>Friends</h1></html>"));
		chatView.add(jpTitle);
		
		//The friends lists
		chatView.add(createChatFriendsList());
		
		//the actions
		chatView.add(createFriendsActions());
		
		return chatView;
	}
	
	private JScrollPane createChatFriendsList() {
		friendList = new JPanel(new BorderLayout());		
		jspFriendList = new JScrollPane(friendList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jspFriendList.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
		
		updateFriendsList();
		return jspFriendList;
	}
	
	private synchronized ChatUser getChatUser(final String user) {
		ChatUser chatUser = chatUsers.get(user);
		if (chatUser == null) {
			chatUser = new ChatUser(user);
			chatUsers.put(user, chatUser);
		}
		return chatUser;
	}
	
	public void update() {
		updateMessages();
		updateFriendsList();
	}
	
	private void updateMessages() {
		if (chatboxController.isLastMessageChanged()) {
			Message message = chatboxController.getMessage();
			ChatUser chatUser = getChatUser(message.getSender());
			chatUser.addTextMessage(message.getMessageValue(), true);
			// if chat not open notify user that he get a new msg
			if (!chatUser.getUser().equals(selectedUser) && chatUser.getUserButtons() != null) {
				chatUser.getUserButtons().setBackground(new Color(155,155,0,125));
			}
		}
	}
	
	private void updateFriendsList() {
		if (friendList == null) {
			friendList = new JPanel(new BorderLayout());
		} else {
			friendList.removeAll();
		}
		if (chatboxController != null) {
			JPanel listPanel = new JPanel(new GridBagLayout());
			Set<String> users = new HashSet<String>();
			users.addAll(friendsController.getActiveFriends());
			users.addAll(chatUsers.keySet());
			listPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = getGridBagConstraints(0);
			for (String user : users) {
				JPanel userPanel = new JPanel(new BorderLayout());
				JButton userButton = getUserButton(user);
				userPanel.add(userButton, BorderLayout.CENTER);
				listPanel.add(userPanel, gbc);
				gbc = getGridBagConstraints(gbc.gridy+1);
				if (selectedUser != null && user.equals(selectedUser)) {
					JPanel chatBox = getChatBoxToDraw(user);
					listPanel.add(chatBox, gbc);
					gbc = getGridBagConstraints(gbc.gridy+1);
				} else {
					//to be sure that the chat box exists
					getChatBoxToDraw(user);
				}
			}
			Set<String> disconnectUsers = new HashSet<String>();
			disconnectUsers.addAll(chatUsers.keySet());
			disconnectUsers.removeAll(friendsController.getActiveFriends());
			setDisconnectUserOptions(disconnectUsers);
			friendList.add(listPanel, BorderLayout.NORTH);
		}
		friendList.revalidate();
		friendList.repaint();
	}
	
	private final GridBagConstraints getGridBagConstraints(int gridy) {
		GridBagConstraints gbc = new GridBagConstraints();
		//in order to fill the width of the container
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		//in order to have all element verticaly align with each other
		gbc.gridx = 0;
		gbc.gridy = gridy;
		return gbc;
	}
	
	private final JButton getUserButton(final String user) {
		ChatUser chatUser = getChatUser(user);
		JButton userButton = chatUser.getUserButtons();
		if (userButton == null) {
			userButton = new JButton(user);
			userButton.addActionListener(e -> selectOrUnselectUserChat(user));
			userButton.setBackground(new Color(120,120,120,175));
			chatUser.setUserButtons(userButton);
		}
		return userButton;
	}
	
	private void selectOrUnselectUserChat(final String user) {
		if (selectedUser != null) {
			getChatUser(selectedUser).getUserButtons()
				.setBackground(new Color(120,120,120,175));
		}
		if (selectedUser != null && selectedUser.equals(user)) {
			selectedUser = null;
		} else {
			selectedUser = user;
			getChatUser(user).getUserButtons()
				.setBackground(new Color(200,200,200,175));
		}
		updateFriendsList();
	}

	private final JPanel getChatBoxToDraw(final String user) {
		ChatUser chatUser = getChatUser(user);
		return createChatBox(chatUser);
	}
	
	private final JPanel createChatBox(final ChatUser chatUser) {
		JPanel userChatBox = new JPanel(new BorderLayout());
		// 40 equals approximatively the size of the write area's height
		userChatBox.setPreferredSize(new Dimension(this.getWidth()-20, READ_AREA_HEIGHT+50));
		//Create read text area and put it into a jscrollpane to make it readable
		JTextPane readTextArea = chatUser.getReadTextArea();
		if (readTextArea == null) {
			readTextArea = createReadTextArea();
			chatUser.setReadTextArea(readTextArea);
		}
		readTextArea.setPreferredSize(new Dimension(this.getWidth()-20, READ_AREA_HEIGHT));
		JScrollPane jspChatBox = new JScrollPane(readTextArea, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jspChatBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		jspChatBox.setPreferredSize(new Dimension(this.getWidth()-20, READ_AREA_HEIGHT));
		
		userChatBox.add(jspChatBox, BorderLayout.NORTH);
		
		//Create the write input text area and put it into a jscrollpane
		JTextArea writeTextArea = chatUser.getWriteTextArea();
		if (writeTextArea == null) {
			writeTextArea = createWriteTextArea(chatUser);
			chatUser.setWriteTextArea(writeTextArea);
		} else {
			writeTextArea.setEditable(true);
		}
		int columns = Math.max((int) Math.round(this.getWidth()/18.8), 0);
		writeTextArea.setColumns(columns);
		writeTextArea.invalidate();
		JScrollPane jspTextField = new JScrollPane(writeTextArea, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		userChatBox.add(jspTextField, BorderLayout.SOUTH);
		
		return userChatBox;
	}
	
	private JTextPane createReadTextArea() {
		JTextPane readTextArea = new JTextPane();
		readTextArea.setSize(this.getWidth()-15, READ_AREA_HEIGHT);
		readTextArea.setPreferredSize(new Dimension(this.getWidth()-15, READ_AREA_HEIGHT));
		readTextArea.setMaximumSize(new Dimension(this.getWidth()-15, READ_AREA_HEIGHT));
		readTextArea.setMinimumSize(new Dimension(MIN_CHATVIEW_WIDTH-10, READ_AREA_HEIGHT));
		readTextArea.setEditable(false);
		((DefaultCaret) readTextArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		return readTextArea;
	}
	
	private JTextArea createWriteTextArea(final ChatUser chatUser) {
		JTextArea writeTextArea = new JTextArea();
		// the two lines below allow to resize the input messages
		writeTextArea.setLineWrap(true);
		writeTextArea.setWrapStyleWord(true);
		writeTextArea.setRows(2);
		// prepare the chat
		List<String> receivers = new ArrayList<String>();
		receivers.add(chatUser.getUser());
		writeTextArea.addKeyListener(new SendingMessageKeyListener(chatUser, writeTextArea,
				chatboxController, receivers, username));
		return writeTextArea;
	}	
	
	private void setDisconnectUserOptions(final Set<String> users) {
		for (String user : users) {
			if (!friendsController.getActiveFriends().contains(user)) {
				ChatUser chatUser = getChatUser(user);
				if (chatUser.getWriteTextArea() != null) {
					chatUser.getWriteTextArea().setEditable(false);
				}
				JButton btn = chatUser.getUserButtons();
				btn.setBackground(new Color(255, 0, 0, 125));
				btn.getParent().add(createRemoveUserButton(user), BorderLayout.EAST);
			}
		}
	}
	
	private JButton createRemoveUserButton(final String user) {
		JButton btn = new JButton("x");
		btn.addActionListener(e -> {removeUserChat(user);});
		return btn;
	}
	
	private void removeUserChat(final String user) {
		if (!friendsController.getActiveFriends().contains(user)){
			chatUsers.remove(user);
			updateFriendsList();
		}
	}
	
	private Box createFriendsActions() {
		Box friendsActionsBox = Box.createHorizontalBox();
		friendsActionsBox.setBackground(Color.GRAY);
		friendsActionsBox.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
		friendsActionsBox.setMinimumSize(new Dimension(MIN_CHATVIEW_WIDTH-10, ACTIONS_BUTTONS_HEIGHT-10));
		friendsActionsBox.setMaximumSize(new Dimension(dimension.width-10, ACTIONS_BUTTONS_HEIGHT));
		friendsActionsBox.setSize(friendsActionsBox.getMinimumSize());
		
		//Buttons Panel
		JPanel actionsPanel = new JPanel();
		jbAddFriend = new JButton(new ImageIcon(ImageLoader.prepareImage("images/addFriend.jpg")));
		jbAddFriend.setOpaque(false);
		jbAddFriend.addActionListener(e -> {addFriend(e);});
		actionsPanel.add(jbAddFriend);
		jbRemoveFriend = new JButton(new ImageIcon(ImageLoader.prepareImage("images/remFriend.jpg")));
		jbRemoveFriend.addActionListener(e -> {removeFriend(e);});
		actionsPanel.add(jbRemoveFriend);
		
		friendsActionsBox.add(actionsPanel);
		return friendsActionsBox;
	}
	
	private void addFriend(ActionEvent e) {
		try {
			JOptionPane.showMessageDialog(this.getParent(), new FriendsActionsView(true), "Add a friend",
				JOptionPane.PLAIN_MESSAGE);
		} catch (ConnectionException c) {
			JOptionPane.showMessageDialog(this.getParent(), "Connection problem!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	private void removeFriend(ActionEvent e) {
		try {
			JOptionPane.showMessageDialog(this.getParent(), new FriendsActionsView(false), "Remove a friend",
				JOptionPane.PLAIN_MESSAGE);
		} catch (ConnectionException c) {
			JOptionPane.showMessageDialog(this.getParent(), "Connection problem!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void updateSize(int width) {
		updateFriendsList();
	}
	
	private class ResizeListener extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent e) {
			updateSize(e.getComponent().getWidth());
		}
	}
	
}
