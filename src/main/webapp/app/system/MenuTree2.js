Ext.ns('app.system');
app.system.MenuTree = Ext.extend(Ext.tree.TreePanel, {
	lines : false,
	initComponent : function() {
		var me = this;
		var root = new Ext.tree.AsyncTreeNode({
			id : 'root',
			expand : true,
			expanded : true
		});
		var sub1 = new Ext.tree.TreeNode({
			text : "机构管理",
			iconCls : 'dept30',
			iconCls2 : 'dept16',
			file : 'app.view.dept.DeptTreePanel',
			singleClickExpand : true
		});
		var sub2 = new Ext.tree.TreeNode({
			text : "机器管理",
			iconCls : 'machine30',
			iconCls2 : 'machine16',
			file : 'app.view.dept.MachinePanel',
			singleClickExpand : true
		});
		var sub3 = new Ext.tree.TreeNode({
			text : "用户管理",
			iconCls : 'user30',
			iconCls2 : 'user16',
			file : 'app.view.dept.UserGridPanel',
			singleClickExpand : true
		});
		var sub4 = new Ext.tree.TreeNode({
			text : "文件共享",
			iconCls : 'fileshare30',
			iconCls2 : 'fileshare16',
			file : 'app.view.fileshare.FileShareGridPanel',
			singleClickExpand : true
		});
		var sub5 = new Ext.tree.TreeNode({
			text : '背景图片管理',
			iconCls : 'backImg',
			iconCls2 : 'backImg2',
			file : 'app.view.background.BackGroundPanel',
			singleClickExpand : true
		});
		root.appendChild(sub1);
		root.appendChild(sub2);
		root.appendChild(sub3);
		root.appendChild(sub4);
		root.appendChild(sub5);
		me.root = root;
		me.rootVisible = false;
		app.system.MenuTree.superclass.initComponent.apply(this, arguments);
	}
});
Ext.reg("menutree2", app.system.MenuTree);
