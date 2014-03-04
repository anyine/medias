Ext.ns('app.view.tasksend');
app.view.tasksend.TaskSendEditWindow = Ext.extend(Ext.Window, {
	title : '编辑',
	width : 320,
	height : 228,
	buttonAlign : 'center',
	initComponent : function() {
		var me = this;

		var store = new Ext.data.Store({
					autoLoad : true,
					proxy : new Ext.data.HttpProxy({
								url : "cn/wizool/bank/servlet/TaskServlet?method=get"
							}),
					reader : new Ext.data.JsonReader({
								root : 'root',
								totalProperty : 'total',
								fields : ['id', 'name']
							})
				});
		store.on('beforeload', function(thiz) {
					Ext.apply(thiz.baseParams, {
								start : 0,
								limit : 1000
							});
				});
		me.items = [{
			xtype : 'form',
			bodyStyle : 'padding: 20px; background-color: #DFE8F6',
			defaults : {
				labelWidth : 30,
				allowBlank : false
			},
			defaultType : 'textfield',
			items : [{
						fieldLabel : '名称',
						name : 'name'
					}, {
						fieldLabel : '日期',
						name : 'date',
						validator : function() {
							var value = this.getValue();
							var values = value.split("-");
							if (value.length == 10 && value.indexOf("-") != -1
									&& values.length == 3) {
								return true;
							} else {
								return false;
							}
						}
					}, {
						fieldLabel : '时间',
						name : 'time',
						validator : function() {
							var value = this.getValue();
							var values = value.split(":");
							if (value.length == 5 && value.indexOf(":") != -1
									&& values.length == 2 && values[0] >= 0
									&& values[0] < 24 && values[1] >= 0
									&& values[1] < 60) {
								return true;
							} else {
								return false;
							}
						}
					}, {
						fieldLabel : '选择机构',
						value : '点击查看',
						readOnly : true,
						listeners : {
							focus : function() {
								var win = new app.view.dept.DeptCheckWindow({
											idsSelected : me.deptIds
										});
								win.on('beforeclose', function() {
											me.findByType('hidden')[0]
													.setValue(this.ids);
										});
								win.show();
							}
						}
					}, {
						xtype : 'combo',
						fieldLabel : '选择任务',
						store : store,
						editable : false,
						triggerAction : 'all',
						valueField : 'id',
						mode : 'local',
						displayField : 'name',
						width : 130,
						listeners : {
							select : function(a, b) {
								me.findByType('hidden')[1]
										.setValue(b.get('id'));
							}
						}
					}, {
						xtype : 'hidden',
						name : 'dept'
					}, {
						xtype : 'hidden',
						name : 'task'
					}]
		}]

		me.buttons = [{
					text : '提交',
					handler : me.save,
					scope : me
				}, {
					text : '取消',
					handler : function() {
						me.close()
					}
				}];

		app.view.tasksend.TaskSendEditWindow.superclass.initComponent.apply(
				this, arguments);
	},
	save : function() {
		var me = this;
		var form = me.findByType('form')[0];
		form.getForm().submit({
					url : 'cn/wizool/bank/servlet/TaskSendServlet',
					params : {
						method : 'create'
					},
					success : function(form, action) {
						var result = action.result;
						if (result.success) {
							Ext.example.msg("提示", "操作成功");
							me.thisStore.load({
										params : {
											start : 0,
											limit : 25
										}
									});
						}
						me.close();
					},
					failure : function(form, action) {
						var result = action.result;
						me.thisStore.load({
									params : {
										start : 0,
										limit : 25
									}
								});
						Ext.Msg.alert("提示", result.message);
					}
				});
	}
});