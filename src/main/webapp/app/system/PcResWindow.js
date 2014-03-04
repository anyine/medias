Ext.ns('app.system');
app.system.PcResWindow = Ext.extend(Ext.Window, {
	title : '网点注册',
	buttonAlign : 'center',
	width : 300,
	height : 250,
	initComponent : function() {
		var me = this;
		var typeSt = new Ext.data.SimpleStore({
			fields : [ 'id', 'name' ],
			data : [ [ '广告', '广告' ], [ '培训', '培训' ] ]
		});

		me.items = {
			xtype : 'form',
			bodyStyle : 'padding: 20px; background-color: #DFE8F6',
			defaults : {
				labelWidth : 40
			},
			defaultType : 'textfield',
			items : [ {
				xtype : 'textfield',
				fieldLabel : '选择机构',
				value : '点击查看',
				readOnly : true,
				listeners : {
					focus : function() {
						var win = new app.view.dept.BranchCheckWindow();
						win.on('beforehide', function() {
							me.findByType('hidden')[0].setValue(this.id);
							me.findByType('textfield')[0].setValue(this.name);
						});
						win.show();
					}
				}
			}, {
				fieldLabel : '计算机名称',
				name : 'name',
				allowBlank : false
			}, {
				xtype : 'combo',
				fieldLabel : '类型',
				store : typeSt,
				editable : false,
				triggerAction : 'all',
				valueField : 'name',
				mode : 'local',
				displayField : 'name',
				name : 'type',
				width : 130,
				allowBlank : false
			}, {
				fieldLabel : '联系人',
				name : 'linkman'
			}, {
				fieldLabel : '电话',
				name : 'phone'
			}, {
				fieldLabel : '座机',
				name : 'mobilephone'
			}, {
				xtype : 'hidden',
				name : 'pid'
			} ]
		}

		me.buttons = [ {
			text : '注册',
			handler : me.login,
			scope : me
		}, {
			text : '重置',
			handler : function() {
				me.findByType("form")[0].getForm().reset();
			}
		} ]

		app.system.PcResWindow.superclass.initComponent.apply(this, arguments);
	},
	login : function() {
		var me = this;
		var form = me.findByType('form')[0];
		form.getForm().submit({
			url : 'cn/wizool/bank/servlet/DepartmentServlet?method=editMachine',
			success : function(form, action) {
				var result = action.result;
				if (result.success) {
					me.close();
					Ext.Msg.alert("提示", "注册成功");
					var type = me.findByType('combo')[1].getValue();
					if (type == "广告") {
						window.location.href = "Media1.html";
					} else if (type == "培训") {
						window.location.href = "Train1.html";
					}
				}
			},
			failure : function(form, action) {
				var result = action.result;
				Ext.Msg.alert("提示", result.message);
			}
		});
	}
});