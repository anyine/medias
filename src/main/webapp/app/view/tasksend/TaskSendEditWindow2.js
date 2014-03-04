Ext.ns('app.view.tasksend');
app.view.tasksend.TaskSendEditWindow2 = Ext.extend(Ext.Window, {
	title : '编辑',
	width : 320,
	height : 150,
	buttonAlign : 'center',
	initComponent : function() {
		var me = this;

		var typeSt = new Ext.data.SimpleStore({
					fields : ['id', 'name'],
					data : [['开', '开'], ['关', '关']]
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
						xtype : 'combo',
						fieldLabel : '调度开关',
						store : typeSt,
						editable : false,
						triggerAction : 'all',
						valueField : 'name',
						mode : 'local',
						displayField : 'name',
						name : 'type',
						width : 80,
						listeners : {
							select : function(a, b) {
								var s = b.get("name");
								if (s == "开") {
									me.findByType('hidden')[0].setValue('true');
								} else if (s == "关") {
									me.findByType('hidden')[0]
											.setValue('false');
								}

							}
						}
					}, {
						xtype : 'textfield',
						fieldLabel : '调度方式',
						name : 'schedule',
						allowBlank : true,
						value : ''
					}, {
						xtype : 'hidden',
						name : 'enable'
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

		app.view.tasksend.TaskSendEditWindow2.superclass.initComponent.apply(
				this, arguments);
	},
	save : function() {
		var me = this;
		var form = me.findByType('form')[0];

		form.getForm().submit({
					url : 'cn/wizool/bank/servlet/TaskSendServlet',
					params : {
						method : 'modifyEnable',
						id : me.thisId
					},
					success : function(form, action) {
						var result = action.result;
						if (result.success) {
							Ext.example.msg("提示", "操作成功");
						}
						me.thisStore.load({
									params : {
										start : 0,
										limit : 25
									}
								});
						me.close();
					},
					failure : function(form, action) {
						console.log(action);
						var result = action.result;
						Ext.Msg.alert("提示", result.message);
					}
				});
	}
});