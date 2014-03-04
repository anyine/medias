// 左侧拉链菜单
function CreateTreePanel(menuPanel, tabPanel) {
	var chObj = {};
	var menuTree = createTree();
	menuPanel.add(menuTree);
	menuPanel.doLayout();
	menuTree
			.on(
					'click',
					function(node, e) {
						var attr = node.attributes;
						if (attr.type == "window") {
							eval("var win =  new " + attr.file
									+ "();win.show()");
						} else if (attr.type == "exit") {
							Ext.MessageBox.confirm('提示', '您确定退出系统吗？', function(
									btn) {
								if (btn == 'yes') {
									document.cookie = "userName=null;path=/";
									window.location.href = "/bank";
								}
							});
						} else {
							var fileUrl = attr.file;
							if (attr.type == null || attr.type == '') {
								var tabItem = eval("new "
										+ fileUrl
										+ "({closable : true,iconCls : 'user'});");
							} else {

								var tabItem = eval("new "
										+ fileUrl
										+ "({closable : true,iconCls : 'user',type:attr.type,id:attr.type,title:attr.title});");
							}
							if (tabPanel.getComponent(chObj[tabItem.title]) == null) {
								tabPanel.add(tabItem);
								tabPanel.setActiveTab(tabItem);
								chObj[tabItem.title] = tabItem.id;
							} else {
								tabPanel.setActiveTab(chObj[tabItem.title]);
							}
						}
					});

	var task = {
		run : function() {
			Ext.Ajax.request({
				url : 'cn/wizool/bank/servlet/InitServlet?method=getAppStatus',
				success : function() {

				},
				failure : function() {
					document.cookie = "userName=null;path=/";
					alert("与服务器失去链接");
					window.location.href = "/bank";
				}
			});
		},
		interval : 3000
	}
	var runner = new Ext.util.TaskRunner();
	runner.start(task);

}

createTree = function() {
	var root = new Ext.tree.AsyncTreeNode({
		id : 'root',
		expand : true,
		expanded : true
	});
	var sub1 = new Ext.tree.TreeNode({
		title : "机构管理",
		text : "机构管理",
		iconCls : 'test',
		file : 'app.view.dept.DeptTreePanel',
		singleClickExpand : true
	});
	var sub2 = new Ext.tree.TreeNode({
		title : "文件共享",
		text : "文件共享",
		// iconCls : 'fileshare',
		file : 'app.view.fileshare.FileShareGridPanel',
		singleClickExpand : true
	});
	var sub4 = new Ext.tree.TreeNode({
		title : "广告媒体",
		text : "广告媒体",
		// iconCls : 'medium',
		type : 'medium',
		file : 'app.view.media.MediaGridPanel',
		singleClickExpand : true
	});
	var sub5 = new Ext.tree.TreeNode({
		title : "通知公告",
		text : "通知公告",
		type : 'notice',
		// iconCls : 'notice',
		file : 'app.view.notice.NoticeGridPanel',
		singleClickExpand : true
	});
	var sub6 = new Ext.tree.TreeNode({
		title : "培训资料",
		text : "培训资料",
		// iconCls : 'train',
		type : 'train',
		file : 'app.view.train.TrainGridPanel',
		singleClickExpand : true
	});
	var sub7 = new Ext.tree.TreeNode({
		title : "实时监控",
		text : '实时监控',
		// iconCls : 'exit',
		file : 'app.system.ControlPanel',
		singleClickExpand : true
	});
	var sub8 = new Ext.tree.TreeNode({
		title : "系统更新",
		// iconCls : 'exit',
		type : 'window',
		text : '系统更新',
		file : 'app.system.UpdateSystem',
		singleClickExpand : true
	});
	var sub9 = new Ext.tree.TreeNode({
		title : "修改密码",
		text : "修改密码",
		type : 'window',
		// iconCls : 'password',
		file : 'app.system.ModifyPassword',
		singleClickExpand : true
	});
	var sub10 = new Ext.tree.TreeNode({
		title : "退出系统",
		text : "退出系统",
		file : '',
		type : 'exit',
		// iconCls : 'exit',
		singleClickExpand : true
	});
	root.appendChild(sub1);
	root.appendChild(sub2);
	root.appendChild(sub4);
	root.appendChild(sub5);
	root.appendChild(sub6);
	root.appendChild(sub7);
	root.appendChild(sub8);
	root.appendChild(sub9);
	root.appendChild(sub10);
	var treepanel = new Ext.tree.TreePanel({
		xtype : 'treepanel',
		lines : false,
		root : root,
		rootVisible : false
	});
	return treepanel;
}
