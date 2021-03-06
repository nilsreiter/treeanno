
var documents_selected_for_diff = new Array();
var max_documents_for_diff = 2;


function init_projects() {
	init_all();
	$("#documentuploaddialog").dialog({
		hide:false,
		title:i18n.t("new_document.title"),
		buttons:[{
			text:i18n.t("new_document.submit"),
			click:function() {
				$("#documentuploaddialog form").submit();
			}
		}]
	});
	$("#documentuploaddialog").dialog("close");

	if (admin) {
		$("button#new_user_open_dialog").button({
			label:i18n.t("new_user.open_dialog"),
			icons: { primary: "ui-icon-plus", secondary: null },
			text:configuration["treeanno.ui.showTextOnButtons"]
		}).click(function() {
			$("#newuserdialog").dialog("open");
		});
		
		$("button#new_project_open_dialog").button({
			label:i18n.t("new_project.open_dialog"),
			icons: { primary: "ui-icon-plus", secondary: null },
			text:configuration["treeanno.ui.showTextOnButtons"]
		}).click(function() {
			$("#newprojectdialog").dialog("open");
		});

		$("#newuserdialog").dialog({
			hide : false,
			title : i18n.t("new_user.title"),
			buttons : [ {
				text : i18n.t("new_user.submit"),
				click : function() {
					$("#newuserdialog").dialog("close");
					$.ajax({
						type: "POST",
						contentType: "application/json",
						url: "rpc/user/create",
						data: JSON.stringify({
							name: $("#new_user_name").val(),
							email: $("#new_user_email").val(),
							language: $("#new_user_language").val()
						}),
						success: show_userlist,
						error: function(jqXHR, textStatus, errorThrown) {
							noty({
								text:textStatus+": "+errorThrown,
								timeout:false,
								type:'error',
								modal:true
							});
							console.log(textStatus, errorThrown);
						}
					});
				}
			} ]
		});
		$("#newuserdialog").dialog("close");
		
		$("#newprojectdialog").dialog({
			hide : false,
			title : i18n.t("new_project.title"),
			buttons : [ {
				text : i18n.t("new_project.submit"),
				click : function() {
					$("#newprojectdialog").dialog("close");
					$.ajax({
						type: "POST",
						contentType: "application/json",
						url: "rpc/projects",
						data: JSON.stringify({
							name: $("#new_project_name").val(),
							type: $("#new_project_type").val()
						}),
						success: function(data, a2, a3) {
							console.log(data);
							window.location.href = "projects.jsp?projectId="+data.id
						},
						error: function(jqXHR, textStatus, errorThrown) {
							noty({
								text:textStatus+": "+errorThrown,
								timeout:false,
								type:'error',
								modal:true
							});
							console.log(textStatus, errorThrown);
						}
					});
				}
			} ]
		});
		$("#newprojectdialog").dialog("close");
	} else {
		$("#newuserdialog").hide();
		$("#newprojectdialog").hide();
		$("#new_project_open_dialog").hide();
	}

	
	$(".splitleft").append("<img src=\"gfx/loading1.gif\" />");
	$(".splitleft #projectlistarea").hide();
	jQuery.getJSON("rpc/AccessLevel/all?userId=-1", function(data) {
		for (var i = 0; i < data.length; i++) {
			var tr = document.createElement("tr");
			var id=data[i]['id'];
			$(tr).append("<td>"+data[i]['project']['id']+"</td>");
			$(tr).append("<td>"+data[i]['project']['name']+"</td>");
			$(tr).append("<td><button class=\"button_open project "+data[i]['project']['id']+"\"></button></td>");
			$(tr).find("button.button_open").button({
				label: i18n.t("project_action_open"),
				icons:{primary:"ui-icon-folder-collapsed",secondary:null},
				text:configuration["treeanno.ui.showTextOnButtons"],
				disabled:(data[i]['AccessLevel'] == Perm.NOACCESS)
			}).click({'projectId':data[i]['project']['id']}, function(event) {	
				show_documentlist(event.data['projectId']); 
			});
			$("#projectlistarea table tbody").append(tr);
		};
		$(".splitleft img").remove();
		$(".splitleft #projectlistarea").show();
		if (selected != -1)
			show_documentlist(selected);

	});
	
	show_userlist();
	
	$( "button.button_edit_user" ).button({
		icons: { primary: "ui-icon-person", secondary:null },
		text:configuration["treeanno.ui.showTextOnButtons"]
	});
}

function show_exportoptions(projectId,document) {
	
	$("#annodoclistarea").remove();
	$("#topbar .left .adocname").remove();

	$("#content .splitright").append("<div id=\"annodoclistarea\"></div>");
	var area = $("#annodoclistarea");
	
	area.hide();
	
	area.append("<h2>"+i18n.t("exportoptions.title_for_X", {"document":document['name']})+"</h2>");
	$("#topbar .left").append("<span class=\"adocname\">&nbsp;&gt; "+i18n.t("exportoptions.breadcrumb_for_X", {"document":document['name']})+"</span>");
	
	area.append("<div class=\"actionbar\"></div>");
	var abar = area.children(".actionbar");
	abar.append("<button class=\"export_xmi\" name=\"export_xmi\" value=\""+document['id']+"\">"+i18n.t("exportoptions.xmi")+"</button>");
	abar.append("<button class=\"export_par\" name=\"export_par\" value=\""+document['id']+"\">"+i18n.t("exportoptions.par")+"</button>");
	abar.append("<button class=\"export_par_id\" name=\"export_par_id\" value=\""+document['id']+"\">"+i18n.t("exportoptions.par_id")+"</button>");
	abar.append("<button class=\"export_xml\" name=\"export_xml\" value=\""+document['id']+"\">"+i18n.t("exportoptions.xml")+"</button>");
	abar.append("<button class=\"export_dot\" name=\"export_dot\" value=\""+document['id']+"\">"+i18n.t("exportoptions.dot")+"</button>");
	abar.append("<button class=\"export_chart\" name=\"export_chart\" value=\""+document['id']+"\">"+i18n.t("exportoptions.chart")+"</button>");
	
	abar.children(".export_xmi").button({
		label:i18n.t("exportoptions.xmi"),
		text:configuration["treeanno.ui.showTextOnButtons"]
	}).click({
		'documentId':document['id']
	}, function(event) {
		window.location.href="rpc/xmi/"+projectId+"/"+event.data.documentId;
	});
	
	abar.children(".export_par").button({
		label:i18n.t("exportoptions.par"),
		text:configuration["treeanno.ui.showTextOnButtons"]
	}).click({
		'documentId':document['id']
	}, function(event) {
		window.location.href="rpc/par/"+projectId+"/"+event.data.documentId;
	});
	
	abar.children(".export_par_id").button({
		label:i18n.t("exportoptions.par_id"),
		text:configuration["treeanno.ui.showTextOnButtons"]
	}).click({
		'documentId':document['id']
	}, function(event) {
		window.location.href="rpc/par_id/"+projectId+"/"+event.data.documentId;
	});
	
	abar.children(".export_xml").button({
		label:i18n.t("exportoptions.xml"),
		text:configuration["treeanno.ui.showTextOnButtons"]
	}).click({
		'documentId':document['id']
	}, function(event) {
		window.location.href="rpc/xml/"+projectId+"/"+event.data.documentId;
	});
	
	abar.children(".export_dot").button({
		label:i18n.t("exportoptions.dot"),
		text:configuration["treeanno.ui.showTextOnButtons"]
	}).click({
		'documentId':document['id']
	}, function(event) {
		window.location.href="rpc/dot/"+projectId+"/"+event.data.documentId;
	});
	
	abar.children(".export_chart").button({
		label:i18n.t("exportoptions.chart"),
		text:configuration["treeanno.ui.showTextOnButtons"]
	}).click({
		'documentId':document['id']
	}, function(event) {
		window.location.href="rpc/chart/"+projectId+"/"+event.data.documentId;
	});
	
	abar.buttonset();
	
	area.show();
	
}

function show_annodoclist(projectId, id) {
	$("#annodoclistarea").remove();
	$("#topbar .left .adocname").remove();

	$("#content .splitright").append("<div id=\"annodoclistarea\"></div>");
	$("#annodoclistarea").hide();

	jQuery.getJSON("rpc/"+projectId+"/"+id, function(data) {
		var header = false;
		var table = document.createElement("table");

		if ('documents' in data) {
			for (var i = 0; i < data['documents'].length; i++) {
				if (!header) {
					var trh = document.createElement("tr");
					$(trh).append("<th>"+i18n.t("annodoclistarea.id")+"</th>");
					$(trh).append("<th>"+i18n.t("annodoclistarea.username")+"</th>");
					$(trh).append("<th>"+i18n.t("annodoclistarea.mod_date")+"</th>");
					$(trh).append("<th>"+i18n.t("annodoclistarea.actions")+"</th>");
					$(table).append(trh);
					header = true;
				}
				var tr = document.createElement("tr");
				$(tr).append("<td>"+data['documents'][i]['id']+"</td>");
				$(tr).append("<td>"+data['documents'][i]['user']['name']+"</td>");
				$(tr).append("<td>"+data['documents'][i]['modificationDate']+"</td>");
			
				var actionCell = document.createElement("td");
				$(actionCell).append("<button class=\"button_view\" id=\"view-udoc-"+data['documents'][i]['id']+"\" name=\"view\" value=\""+data['documents'][i]['id']+"\">"+i18n.t("annodoclistarea.view")+"</button>");
				$(actionCell).append("<button class=\"button_delete\" id=\"delete-udoc-"+data['documents'][i]['id']+"\" name=\"delete\" value=\""+data['documents'][i]['id']+"\">"+i18n.t("annodoclistarea.delete")+"</button>");
				// if (al >= Perm["PADMINACCESS"]) 
					$(actionCell).append("<input class=\"button_diff\" id=\"diffselect-"+data['documents'][i]['id']+"\" type=\"checkbox\" name=\"diff\" value=\""+data['documents'][i]['id']+"\"/><label for=\"diffselect-"+data['documents'][i]['id']+"\"></label>");

				$(actionCell).buttonset();
				$(tr).append(actionCell);
				$(table).append(tr);
				
				// diff select button
				$(actionCell).find("input.button_diff").button({
					label:i18n.t("parallel.select"),
					icons:{primary:"ui-icon-transferthick-e-w",secondary:null},
					text:configuration["treeanno.ui.showTextOnButtons"]
				}); 
				$(actionCell).find("button.button_view").button({
					label:i18n.t("annodoclistarea.view"),
					icons:{primary:"ui-icon-document", secondary:null},
					text:configuration["treeanno.ui.showTextOnButtons"]
				}).click({'userDocumentId':data['documents'][i]['id']}, function(event) {	
	 				window.location.href="parallel.jsp?userDocumentId="+event.data.userDocumentId;
				});
				$(actionCell).find("button.button_delete").button({
					label:i18n.t("annodoclistarea.delete"),
					icons:{primary:"ui-icon-trash", secondary:null},
					text:configuration["treeanno.ui.showTextOnButtons"]
				}).click({'userDocumentId':data['documents'][i]['id']}, function(event) {
					if (confirm(i18n.t("document_action_delete_confirm"))) {
						jQuery.ajax({
							url:"rpc/c/"+projectId+"/"+id+"/"+event.data.userDocumentId,
							complete:function() {show_annodoclist(id); },
							method:"DELETE",
							dataType:"json"
						});
					}
				});
			}
			$("#annodoclistarea").append("<h2>"+i18n.t("annodoclistarea.title_for_X", {"document":data['src']['name']})+"</h2>");
			$("#annodoclistarea").append(table);
			$("#topbar .left").append("<span class=\"adocname\">&nbsp;&gt; "+i18n.t("annodoclistarea.breadcrumb_for_X", {"document":data['src']['name']})+"</span>");
			$("#annodoclistarea").append("<button id=\"button_open_diff\"></button>");
			
			$("button#button_open_diff").button({
				label:i18n.t("parallel.open_view"),
				icons:{primary:"ui-icon-zoomin",secondary:null},
				text:configuration["treeanno.ui.showTextOnButtons"]
			}).click(function() {
				if($("input.button_diff:checked").length == 2) {
					var doc = new Array();
					$("input.button_diff:checked").each(function(index, element) {
						doc[index] = $(element).val();
					});
	 				window.location.href="parallel.jsp?userDocumentId="+doc[0]+"&userDocumentId="+doc[1];
				}
			});
		} else {
			$("#annodoclistarea").append("<p>"+i18n.t("annodoclistarea.no-documents")+"</p>");
		}
		$("#annodoclistarea").show();

	});

}

function assign_users(user) {
	$("#content .splitright").empty();
	$("#content .splitright").append("<img src=\"gfx/loading1.gif\" />");
	$("#content .splitright").append("<div id=\"projectassignarea\"></div>");
	
	jQuery.getJSON("rpc/AccessLevel/all?userId="+user.id, function(data) {
		var table = document.createElement("table");
		$(table).append("<thead><th>"+i18n.t("project_name")+"</th><th>"+i18n.t("project_access")+"</th></thead>");
		var tbody = document.createElement("tbody");
		for (var i = 0; i < data.length; i++) {
			var tr = document.createElement("tr");
			tr.setAttribute("id", "project-"+data[i].project.id);
			$(tr).append("<td>"+data[i].project.name+"</td>");
			$(tr).append("<td></td>");
			var select = document.createElement("select");
			$(select).attr("data-project-id", data[i].project.id);
			
			
			$(select).append("<option "+(data[i].AccessLevel==0?"selected=\"selected\" ":"")+"value=\"0\">"+i18n.t("perm.no_access")+"</option>");
			$(select).append("<option "+(data[i].AccessLevel==10?"selected=\"selected\" ":"")+"value=\"10\">"+i18n.t("perm.read_access")+"</option>");
			$(select).append("<option "+(data[i].AccessLevel==20?"selected=\"selected\" ":"")+"value=\"20\">"+i18n.t("perm.write_access")+"</option>");
			$(select).append("<option "+(data[i].AccessLevel==25?"selected=\"selected\" ":"")+"value=\"25\">"+i18n.t("perm.padmin_access")+"</option>");
			$(select).append("<option "+(data[i].AccessLevel==30?"selected=\"selected\" ":"")+"value=\"30\">"+i18n.t("perm.admin_access")+"</option>");
			$(select).appendTo($("td:last", tr));
			$(tbody).append(tr);
			$("select", tr).selectmenu({width:200});
		};
		
		table.append(tbody);
		
		$("#projectassignarea").append("<h2>"+i18n.t("projects_for_X", {"username":user['name']})+"</h2>");
		$("#projectassignarea").append(table);
		$("#projectassignarea").append("<button class=\"save\"></button>");
		
		$("#projectassignarea button.save").button({
			label:i18n.t("assign_save"),
			icons:{primary:"ui-icon-disk",secondary:null},
			text:configuration["treeanno.ui.showTextOnButtons"]
		}).click(function() {
			var list = $("#projectassignarea tbody select").map(function(index, element) {
				pid = $(element).attr("data-project-id");
				return {"project": pid, "level": $(element).val()};
			}).get();
			
			console.log(list);
			$.ajax({
				type: "POST",
				contentType: "application/json",
				url: "rpc/user/assign",
				data: JSON.stringify({
					user: user.id,
						levels: list}),
				success: function() { assign_users(user) },
				error: function(jqXHR, textStatus, errorThrown) {
					noty({
						text:textStatus+": "+errorThrown,
						timeout:false,
						type:'error',
						modal:true
					});
					console.log(textStatus, errorThrown);
				}
			});
		});
		
		$("#content .splitright img").remove();

	});

}

function show_userlist() {
		if (admin) {
			$("#useradminarea table tbody").empty();
			$("#useradminarea table").hide();
			$("#useradminarea").append("<img src=\"gfx/loading1.gif\" />");
			jQuery.getJSON("rpc/user/list", function(data) {
				
				for (var i = 0; i < data.length; i++) {
					var tr = document.createElement("tr");
					$(tr).append("<td>"+data[i]['id']+"</td>");
					$(tr).append("<td>"+data[i]['name']+"</td>");
					$(tr).append("<td>"+data[i]['email']+"</td>");
					
					
					var actionCell = document.createElement("td");
					$(actionCell).append("<button class=\"button_user_assign\"></button>");
					$(actionCell).find("button.button_user_assign").button({
						label:i18n.t("user_action_assign"),
						icons:{primary:null,secondary:null},
						text:configuration["treeanno.ui.showTextOnButtons"]
					}).click({
						'user':data[i]
					}, function(event) {
						assign_users(event.data.user);
					});

					$(actionCell).buttonset();

					$(tr).append(actionCell);
					
					$("#useradminarea table tbody").append(tr);
				}
				
				
				
				$("#useradminarea table").show();
				$("#useradminarea img").remove();
			});
		}

}

function show_documentlist(id) {
	$("#content .splitright").empty();
	$("#content .splitright").append("<img src=\"gfx/loading1.gif\" />");
	$("#content .splitright").append("<div id=\"documentlistarea\"></div>");

	$("button.button_open.project").button({
		icons:{primary:"ui-icon-folder-collapsed",secondary:null}
	});
	$("button.button_open.project."+id).button({
		icons:{primary:"ui-icon-folder-open",secondary:null}
	});
	$("#topbar .left .adocname").remove();
	$("#topbar .left .pname").remove();
	$("#documentlistarea").hide();		
	
	jQuery.getJSON("rpc/"+id, function(data) {
		var header = false;
		var table = document.createElement("table");
		var al = data['accesslevel'];
		if ("documents" in data) {
		for (var i = 0; i < data['documents'].length; i++) {
			if (!header) {
				var trh = document.createElement("tr");
				$(trh).append("<th>"+i18n.t("document_id")+"</th>");
				$(trh).append("<th>"+i18n.t("document_name")+"</th>");
				//$(trh).append("<th>"+i18n.t("document_mod_date")+"</th>");
				$(trh).append("<th>"+i18n.t("document_actions")+"</th>");
				$(table).append(trh);
				header = true;
			}
			var tr = document.createElement("tr");
			$(tr).append("<td>"+data['documents'][i]['id']+"</td>");
			$(tr).append("<td>"+data['documents'][i]['name']+"</td>");
			//$(tr).append("<td>"+data['documents'][i]['modificationDate']+"</td>");
			
			var actionCell = document.createElement("td");
			if (al >= Perm["READACCESS"])
				$(actionCell).append("<button class=\"button_open\"></button>");
			if (al >= Perm["PADMINACCESS"])
				$(actionCell).append("<button class=\"button_open_master\"></button>");
			if (al >= Perm["PADMINACCESS"])
				$(actionCell).append("<button class=\"button_rename\">rename</button>")
			if (al >= Perm["PADMINACCESS"])
				$(actionCell).append("<button class=\"button_describe\">describe</button>")
			if (al >= Perm["PADMINACCESS"])
				$(actionCell).append("<button class=\"button_delete\">delete</button>");
			if (al >= Perm["PADMINACCESS"])
				$(actionCell).append("<button class=\"button_view_annodoc\">view annotation</button>");
			if (al >= Perm["PADMINACCESS"])
				$(actionCell).append("<button class=\"button_export\">export</button>");
			
			
			$(actionCell).find("button.button_open").button({
				label:i18n.t("document_action_open"),
				icons:{primary:"ui-icon-document",secondary:null},
				text:configuration["treeanno.ui.showTextOnButtons"]
			}).click({
				'documentId':data['documents'][i]['id']
			}, function(event) {
				window.location.href="main.jsp?documentId="+event.data.documentId;
			});
			$(actionCell).find("button.button_open_master").button({
				label:i18n.t("document_action_open_master"),
				icons:{primary:"ui-icon-document",secondary:null},
				text:configuration["treeanno.ui.showTextOnButtons"]
			}).click({
				'documentId':data['documents'][i]['id']
			}, function(event) {
				jQuery.getJSON("rpc/"+id+"/"+event.data.documentId, function(data) {
					if ('documents' in data && data['documents'].length>0) {
						if (confirm(i18n.t("document_action_open_master_confirm"))) {
							window.location.href="main.jsp?master=master&documentId="+event.data.documentId;							
						}
					} else {
						window.location.href="main.jsp?master=master&documentId="+event.data.documentId;													
					}
				});
				
			});
			$(actionCell).find("button.button_rename").button({
				label:i18n.t("document_action_rename"),
				icons:{primary:"ui-icon-pencil",secondary:null},
				text:configuration["treeanno.ui.showTextOnButtons"]
			}).click({
				'document':data['documents'][i]
			}, function(event) {
				var diagDiv = document.createElement("div");
				$(diagDiv).append(i18n.t("rename_dialog.desc")+"<input type=\"text\" value=\""+event.data.document['name']+"\" />");
				$("body").append(diagDiv);
				
				$(diagDiv).dialog({
					title:i18n.t("rename_dialog.title"),
					dialogClass: "no-close",
					buttons: [{
						text:i18n.t("rename_dialog.cancel"),
			        	click: function() {
			        		$(this).dialog("destroy");
			        		document.getElementsByTagName("BODY")[0].removeChild(diagDiv);
			        	}
					},{ 
						text: i18n.t("rename_dialog.ok"),
						click: function() {
							jQuery.getJSON("rpc/document/rename?name="+$(diagDiv).children("input").val()+"&documentId="+event.data.document['id'], function() {
								show_documentlist(id);
							});
					        $( this ).dialog( "close" );
					    }
					}],
					closeOnEscape: true,
					modal:true	
				}).show();
				
			});
			$(actionCell).find("button.button_describe").button({
				label:i18n.t("document_action_describe"),
				icons:{primary:"ui-icon-pencil",secondary:null},
				text:configuration["treeanno.ui.showTextOnButtons"]
			}).click({
				'document':data['documents'][i]
			}, function(event) {
				var diagDiv = document.createElement("div");
				$(diagDiv).append(i18n.t("describe_dialog.desc")+"<br/><textarea rows=\"5\" cols=\"40\">"+event.data.document['description']+"</textarea>");
				$("body").append(diagDiv);
				
				$(diagDiv).dialog({
					title:i18n.t("describe_dialog.title"),
					dialogClass: "no-close",
					buttons: [{
						text:i18n.t("describe_dialog.cancel"),
			        	click: function() {
			        		$(this).dialog("destroy");
			        		document.getElementsByTagName("BODY")[0].removeChild(diagDiv);
			        	}
					},{ 
						text: i18n.t("describe_dialog.ok"),
						click: function() {
							
							jQuery.getJSON("rpc/document/describe?description="+$(diagDiv).children("textarea").val()+"&documentId="+event.data.document['id'], function() {
								show_documentlist(id);
							});
					        $( this ).dialog( "close" );
					    }
					}],
					closeOnEscape: true,
					modal:true	
				}).show();
				
			});
			
			$(actionCell).find("button.button_delete").button({
				label:i18n.t("document_action_delete"),
				icons:{primary:"ui-icon-trash", secondary:null},
				text:configuration["treeanno.ui.showTextOnButtons"]
			}).click({
				'documentId':data['documents'][i]['id']
			}, function(event) {
				if (confirm(i18n.t("document_action_delete_confirm"))) {
					jQuery.ajax({
						url:"rpc/c/"+id+"/"+event.data.documentId,
						complete:function() {show_documentlist(id); },
						method:"DELETE",
						dataType:"json"
					});
				}
			});
			
			$(actionCell).find("button.button_view_annodoc").button({
				label:i18n.t("document_action_view_annodoc"),
				icons:{primary:"ui-icon-cart", secondary:null},
				text:configuration["treeanno.ui.showTextOnButtons"]
			}).click({
				'documentId':data['documents'][i]['id']
			}, function(event) {
				show_annodoclist(id, event.data.documentId);
			});
			
			
			
			$(actionCell).find("button.button_export").button({
				label:i18n.t("document_action_export_xmi"),
				icons:{primary:"ui-icon-arrowstop-1-s", secondary:null},
				text:configuration["treeanno.ui.showTextOnButtons"]
			}).click({
				'document':data['documents'][i]
			}, function(event) {
				show_exportoptions(id, event.data.document);
				// window.location.href="DocumentHandling?action=export&documentId="+event.data.documentId;
			});
			
			
			
			$(actionCell).buttonset();
			$(tr).append(actionCell);
			$(table).append(tr);
		}
		}
		$("#documentlistarea").append("<h2>"+i18n.t("documents_in_X", {"projectname":data['project']['name']})+"</h2>");
		$("#documentlistarea").append(table);
		$("#topbar .left").append("<span class=\"pname\">&nbsp;&gt; "+data['project']['name']+"</span>");
		
		
		$("#content .splitright img").remove();
		
		

		if (al >= Perm["PADMINACCESS"]) {
			$("#documentuploaddialog input[name='projectId']").attr("value", data['project']['id']);
			$("#documentlistarea").append("<button data-i18n=\"new_document.open_dialog\" id=\"new_document_open_dialog\"></button>");	
			$("button#new_document_open_dialog").button({
				label:i18n.t("new_document.open_dialog"),
				icons: { primary: "ui-icon-plus", secondary: null },
				text:configuration["treeanno.ui.showTextOnButtons"]
			}).click(function() {
				$("#documentuploaddialog").dialog("open");
			});
		}
		$("#documentlistarea").show();	

	});
}

function select_document_for_diff(did) {
	if ($("#diffselect-"+did).val() == did) {
		if (documents_selected_for_diff.push(did) > max_documents_for_diff) {
			var d = documents_selected_for_diff.shift();
			$("#diffselect-"+d).removeAttr('checked');
			$("#diffselect-"+d).button( "refresh" );
		}
	} else {
		documents_selected_for_diff.splice(documents_selected_for_diff.indexOf(did),1);
		$("#diffselect-"+did).button( "refresh" );
	}
}