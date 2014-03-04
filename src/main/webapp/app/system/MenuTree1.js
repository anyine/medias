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
			text : "广告媒体",
			iconCls : 'media30',
			iconCls2 : 'media16',
			file : 'app.view.media.MediaGridPanel',
			singleClickExpand : true
		});
		var sub2 = new Ext.tree.TreeNode({
			text : "通知公告",
			iconCls : 'notice30',
			iconCls2 : 'notice16',
			file : 'app.view.notice.NoticeGridPanel',
			singleClickExpand : true
		});
		var sub3 = new Ext.tree.TreeNode({
			text : "培训资料",
			iconCls : 'trian30',
			iconCls2 : 'trian16',
			file : 'app.view.train.TrainGridPanel',
			singleClickExpand : true
		});
		var sub4 = new Ext.tree.TreeNode({
			text : '实时监控',
			iconCls : 'control30',
			iconCls2 : 'control16',
			file : 'app.system.ControlPanel',
			singleClickExpand : true
		});
		var sub5 = new Ext.tree.TreeNode({
			text : '下载历史',
			iconCls : 'filedownload30',
			iconCls2 : 'filedownload16',
			file : 'app.system.FileDownloadPanel',
			singleClickExpand : true
		});
		var sub6 = new Ext.tree.TreeNode({
			text : '任务日志',
			iconCls : 'tasklog30',
			iconCls2 : 'tasklog16',
			file : 'app.system.TaskLogPanel',
			singleClickExpand : true
		});
		var sub7 = new Ext.tree.TreeNode({
			text : '文件日志',
			iconCls : 'filelog30',
			iconCls2 : 'filelog16',
			file : 'app.system.FileLogPanel',
			singleClickExpand : true
		});
		var sub8 = new Ext.tree.TreeNode({
			text : '滚屏字幕',
			iconCls : 'roll30',
			iconCls2 : 'roll16',
			file : 'app.view.system.RollMassageWindow',
			type : 'window',
			singleClickExpand : true
		});
		var sub9 = new Ext.tree.TreeNode({
			text : '定时关机',
			iconCls : 'killpower30',
			iconCls2 : 'killpower16',
			file : 'app.view.system.KillPowerWindow',
			type : 'window',
			singleClickExpand : true
		});
		var sub10 = new Ext.tree.TreeNode({
			text : '利率牌',
			iconCls : 'config30',
			iconCls2 : 'config16',
			file : 'app.view.system.ConfigEditWindow',
			type : 'window',
			singleClickExpand : true
		});
		root.appendChild(sub1);
		root.appendChild(sub2);
		root.appendChild(sub3);
		root.appendChild(sub4);
		root.appendChild(sub5);
		root.appendChild(sub6);
		root.appendChild(sub7);
		root.appendChild(sub8);
		root.appendChild(sub9);
		root.appendChild(sub10);
		me.root = root;
		me.rootVisible = false;
		app.system.MenuTree.superclass.initComponent.apply(this, arguments);
	}
});
Ext.reg("menutree1", app.system.MenuTree);
