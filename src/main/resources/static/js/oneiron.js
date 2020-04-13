Element.prototype.event = function(e, func){
	return this.addEventListener(e,func);
}

String.prototype.replaceAll = function(org, dest) {
    return this.split(org).join(dest);
}

String.prototype.unescapeHtml = function(){
	return this.replace(/&amp;/g, "&").replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/&quot;/g, "\"");
};
/*String.prototype.unescapeHtml = function(){
	return this.replace(/&amp;/g, "&").replaceAll("&lt;br&gt;", "<br>").replaceAll("&lt;a", "<a").replaceAll("'&gt;", "'>").replaceAll("&lt;/a&gt;", "</a>");
};*/


jQuery.fn.serializeObject = function() {
	var obj = null;
	try {
		if (this[0].tagName && this[0].tagName.toUpperCase() == "FORM") {
			var arr = this.serializeArray();
			if (arr) {
				obj = {};
				jQuery.each(arr, function() {
					/*if(!!this.value){
						if (obj[this.name] != undefined) {
							obj[this.name] = obj[this.name] + "," + this.value;
						} else {
							obj[this.name] = this.value;
						}
					}*/
					if (obj[this.name] != undefined) {
						obj[this.name] = obj[this.name] + "," + this.value;
					} else {
						obj[this.name] = this.value;
					}
				});
			}
		}
	} catch (e) {
		alert(e.message);
	} finally {
	}

	return obj;
};

var getEl = function(id){
	return document.getElementById(id);
};

function ajaxJsonParam(url, param, successfunction, completefunction){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	return $.when($.ajax({
		url : url,
        method: "post",
        type: "json",
        async: true,
        contentType: "application/json; charset=utf-8",
        data : JSON.stringify(param),
        beforeSend : function(xmlHttpRequest){
        	// csrf token header에 작성
            xmlHttpRequest.setRequestHeader(header, token);
        }
	})).done(function(data,status,request){
		if(successfunction != undefined && successfunction != null)
    		successfunction(data);
	}).fail(function(request,status,error) {
		if(request.status == 401) {
    		//alert("다시 로그인하여야 합니다.");
    		//document.location.href="/logout.lims";
    		//throw "Request failed.";
    	} else if(request.status == 404){
    		//alert("요청하신 주소를 찾을 수 없습니다.");
    	} else {
    		if(request.responseText != undefined){
    			//var errorlog = JSON.parse(request.responseText);
    			//alert("에러가 발생했습니다. (" + errorlog.error.excpLogSeqno + ")") ;
    			//throw "Request failed.";
    		}
    	}
	}).always(function(request,status){
    	if(completefunction != undefined && completefunction != null)
    		completefunction(request,status);
	});
}

// class명으로 폼데이터 생성해서 objectList 뱉어줌
function getFormDataByClassName(className){
	var result = new Array();
	var domList = document.getElementsByClassName(className);
	for(var i=0; i<domList.length; i++){
		result.push($("#" + domList[i].id).serializeObject());
	}
	return result;
}

function setLoadingBar(){
	var width = 0;
	var height = 0;
	var left = 0;
	var top = 0;
	width = 50;
	height = 50;
	
	top = ( $(window).height() - height ) / 2 + $(window).scrollTop();
	left = ( $(window).width() - width ) / 2 + $(window).scrollLeft();

	if($("#loadingBar").length != 0) {
		$("#ajax_rejection").show();
	} else {
		var text ='<div id="ajax_rejection" style="background:rgba(0,0,0,0.5); position:absolute; top:0px; left:0px; width:100%; height:100%; z-index:9999; filter:alpha(opacity=50); opacity:alpha*0.5; margin:auto; padding:0; ">'
		text +='<div class="container h-100">'
		text +='<div class="row align-items-center h-100">'
		text +='<div class="col-10 mx-auto">'
		text +='<div class="progress">'
		text +='<div id="loadingBar" class="progress-bar progress-bar-striped progress-bar-animated bg-info" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width:0%"></div>'
		text +='</div>'
		text +='</div>'
		text +='</div>';
		
		$('body').append(text);
	}
	
	var interval = setInterval(increaseMent, 20);
	var count = 0;
	
	
	// loadingbar 증가 애니메이션 넣을려고 해봤음 ㅋㅋ
	function increaseMent(){
		if(count<100)
			count = count+20;
		
		getEl("loadingBar").style.width = count + "%";
		
		if(count == 100)
			clearInterval(interval);
	}
}

/**
 * @param form : 폼 이름 정할것
 * @param target : 어느 div밑에 만들건지?
 * @param data : ajax 데이터를 가져오시오
 **/
 function generateProjectDetailForm(formName, target, data){
 	//data 길이만큼 반복
	if(!!data){
		for(var i = 0; i< data.length; i++){
			addProjectForm(formName, target, data[i]);
	 	}
	} else {
		addProjectForm(formName, target, data);
	}
 }
 
 /**
  * @param form : 폼 이름 정할것
  * @param target : 어느 div밑에 만들건지?
  * @param data : 데이터 넣어줌
  **/
 function addProjectForm(formName, target, data){
	
	//form count. 이거 베이스로 쭉쭉쭉만들어간담
	var formCnt = document.querySelectorAll(".upsertForm").length;
	
	//form 을 감싸는 제일 바깥 div
	var outerFormDiv = document.createElement("div");
	outerFormDiv.classList.add("fade-in-out");
	outerFormDiv.style.opacity = "0";
	 
	//Contents Form
	var contentsForm = document.createElement("form");
	contentsForm.id =  formName + "_" + formCnt;
	contentsForm.classList.add("upsertForm");
	
	//---------------TITLE START----------------------------------------//
	
	// title form group div
	var outerTitleDiv = document.createElement("div");
	outerTitleDiv.classList.add("form-group");
	
	// outer row div
	var outerRowDiv = document.createElement("div");
	outerRowDiv.classList.add("row");
	
	// inner col div 1
	var outerColDiv_1 = document.createElement("div");
	outerColDiv_1.classList.add("col");
	
	// inner col div 2
	var outerColDiv_2 = document.createElement("div");
	outerColDiv_2.classList.add("col");
	
	// minus button 
	var minusBtn = document.createElement("button");
	//minusBtn.classList.add("btn", "btn-danger", "float-right");
	// iexplore에서 여러개 안되서 각자로 분리함... 하...
	minusBtn.classList.add("btn");
	minusBtn.classList.add("btn-danger");
	minusBtn.classList.add("float-right");
	minusBtn.type = "button";
	minusBtn.id = "btnMinus_"+ formCnt;
	
	// minus button icon 
	var minusBtnInnerI = document.createElement("i");
	//minusBtnInnerI.classList.add("fa", "fa-minus");
	minusBtnInnerI.classList.add("fa");
	minusBtnInnerI.classList.add("fa-minus");
	minusBtnInnerI.ariaHidden = "true";
	
	//title label
	var innerTitleLbl = document.createElement("label");
	innerTitleLbl.classList.add("col-form-label");
	innerTitleLbl.htmlFor = "title_" + formCnt;
	innerTitleLbl.innerHTML = "Project Name";
	
	// title input
	var innerTitleInput = document.createElement("input");
	innerTitleInput.classList.add("form-control");
	innerTitleInput.classList.add("require");
	innerTitleInput.placeholder = "Project Name";
	innerTitleInput.id = "title_" + formCnt;
	innerTitleInput.name = "title";
//	innerTitleInput.maxLength = 20;
	innerTitleInput.value = (!data ? "" : data.title);
	
	// Col div안에 label을 집어넣고 세트가된걸 row 안에 집어넣음
	outerColDiv_1.appendChild(innerTitleLbl);
	outerRowDiv.appendChild(outerColDiv_1);
	
	// button 안에 icon 집어넣고 세트가된걸 col 안에 집어넣음, 맨윗줄은 삭제버튼 생성안함
	
	if(formCnt !=0){
		minusBtn.appendChild(minusBtnInnerI);
		outerColDiv_2.appendChild(minusBtn);
	}
	
	// col div 안에 button 세트를 집어넣음.
	outerRowDiv.appendChild(outerColDiv_2);
	
	//완성된걸 맨 바깥 부모에게 전달해줌
	outerTitleDiv.appendChild(outerRowDiv);
	outerTitleDiv.appendChild(innerTitleInput);
	//-------------------TITLE END---------------------------//
	
	//-----------PROJECT DATE START-------------------------//
	
	// date group row div
	var dateDiv = document.createElement("div");
	dateDiv.classList.add("row");
	
	//date label, input div
	var outerStDateDiv = document.createElement("div");
	outerStDateDiv.classList.add("col");
	outerStDateDiv.classList.add("form-group");
	
	//start date label
	var innerStDateLbl = document.createElement("label");
	innerStDateLbl.classList.add("col-form-label");
	innerStDateLbl.htmlFor = "stDate_" + formCnt;
	innerStDateLbl.innerHTML = "Project Start";
	
	// start date input
	var innerStDateInput = document.createElement("input");
	innerStDateInput.classList.add("form-control");
	innerStDateInput.classList.add("datepicker");
	innerStDateInput.classList.add("require");
	innerStDateInput.placeholder = "Project Start Date";
	innerStDateInput.id = "stDate_" + formCnt;
	innerStDateInput.name = "stDate";
	innerStDateInput.maxLength = 10;
	innerStDateInput.value = (!data ? "" : data.stDate);
	
	// start date, label 담아둡니다.
	outerStDateDiv.appendChild(innerStDateLbl);
	outerStDateDiv.appendChild(innerStDateInput);
	
	//----------------start, end date 구분선-------------------------//
	
	//date label, input div
	var outerEndDateDiv = document.createElement("div");
	outerEndDateDiv.classList.add("col");
	outerEndDateDiv.classList.add("form-group");
	
	//end date label
	var innerEndDateLbl = document.createElement("label");
	innerEndDateLbl.classList.add("col-form-label");
	innerEndDateLbl.htmlFor = "endDate_" + formCnt;
	innerEndDateLbl.innerHTML = "Project End";
	
	// end date input
	var innerEndDateInput = document.createElement("input");
	innerEndDateInput.classList.add("form-control");
	innerEndDateInput.classList.add("datepicker");
	innerEndDateInput.classList.add("require");
	innerEndDateInput.placeholder = "Project End Date";
	innerEndDateInput.id = "endDate_" + formCnt;
	innerEndDateInput.name = "endDate";
	innerEndDateInput.maxLength = 10;
	innerEndDateInput.value = (!data ? "" : data.endDate);
	
	outerEndDateDiv.appendChild(innerEndDateLbl);
	outerEndDateDiv.appendChild(innerEndDateInput);
	
	
	dateDiv.appendChild(outerStDateDiv);
	dateDiv.appendChild(outerEndDateDiv);
	//--------------End Date End ---------------------------
	
	//---------------skill Start-----------------------------------
	
	// skill form group div
	var outerSkillDiv = document.createElement("div");
	outerSkillDiv.classList.add("form-group");
	
	// skill label
	var innerSkillLbl = document.createElement("label");
	innerSkillLbl.classList.add("col-form-label");
	innerSkillLbl.htmlFor = "content_" + formCnt;
	innerSkillLbl.innerHTML = "Skill";
	
	// skill Input
	var innerSkillInput = document.createElement("input");
	innerSkillInput.classList.add("form-control");
	innerSkillInput.classList.add("autosize");
	innerSkillInput.classList.add("require");
	innerSkillInput.placeholder = "Skills";
	innerSkillInput.id = "skill_" + formCnt;
	innerSkillInput.name = "skill";
	innerSkillInput.rows = 10;
	innerSkillInput.value = (!data ? "" : data.skill);
	
	outerSkillDiv.appendChild(innerSkillLbl);
	outerSkillDiv.appendChild(innerSkillInput);
	
	//---------------skill end-------------------------
	
	//---------------Project Detail Contents Start-----------------------------------
	
	// contents form group div
	var outerContentsDiv = document.createElement("div");
	outerContentsDiv.classList.add("form-group");
	
	// Contents label
	var innerContentsLbl = document.createElement("label");
	innerContentsLbl.classList.add("col-form-label");
	innerContentsLbl.htmlFor = "content_" + formCnt;
	innerContentsLbl.innerHTML = "Project Detail";
	
	// content TextArea
	var innerContentTxtArea = document.createElement("textarea");
	innerContentTxtArea.classList.add("form-control");
	innerContentTxtArea.classList.add("autosize");
	innerContentTxtArea.classList.add("require");
	innerContentTxtArea.placeholder = "Project Detail Contents";
	innerContentTxtArea.id = "content_" + formCnt;
	innerContentTxtArea.name = "content";
	innerContentTxtArea.rows = 10;
	innerContentTxtArea.value = (!data ? "" : data.content.unescapeHtml());
	
	outerContentsDiv.appendChild(innerContentsLbl);
	outerContentsDiv.appendChild(innerContentTxtArea);
	
	//---------------Project Detail end-------------------------
	
	//-----------------divide line start---------------------------
	
	var divideLine = document.createElement("hr");
	divideLine.classList.add("my-4");
	
	//------------------divide line end------------------------------
	
	// 그냥 구분선 조건되면넣엇음
	if(formCnt != 0){
		contentsForm.appendChild(divideLine);
	}
	
	//form 안에 다 쑤셔박음
	contentsForm.appendChild(outerTitleDiv);
	contentsForm.appendChild(dateDiv);
	contentsForm.appendChild(outerSkillDiv);
	contentsForm.appendChild(outerContentsDiv);
	
	//맨바깥 div에 집어넣음
	outerFormDiv.appendChild(contentsForm);
	
	var targetElement = getEl(target);
	
	if(!!targetElement){
		getEl("project").appendChild(outerFormDiv);
		setTimeout(function(){
			outerFormDiv.style.opacity = "1";
		},100);
		
	}
	
	//------------------------폼 완성 끝! 그리고 버튼이벤트 붙임!-----------------------------------
	minusBtn.event("click", function(){
		
		if(confirm("삭제 고고?")){
			outerFormDiv.style.opacity = "0";
			
			setTimeout(function(){
				targetElement.removeChild(outerFormDiv);
			},100);
		}
	});
	
	$('.datepicker').datepicker({
        clearBtn: true,
        format: "yyyy-mm-dd"
    });
 }
 
 // 필수값 입력됐는지 안됐는지 확인하는곳
 function validataion(){
	 
	var result = true;
	 
	//해당클래스 가진거 뒤짐
	var domList = document.getElementsByClassName("require");
	for(var i=0; i<domList.length; i++){
		if(!domList[i].value)
			return false;
	}
	
	return result;
 }
 
 /**
  * 
  * @param appendDivId : 어디에 붙일건가?
  * @param list :  데이터 리스트
  * @returns void
  */
 function viewProjectList(appendDivId, list){
	for(var data in list){
		var text = '<div class="card mb-3 mt-20 wd100p">';
			text += '<h3 class="card-header">'
			text += '	<span id="title">' + ( parseInt(data) + 1) + '. ' + list[data]["title"] + '</span>'
			text += '</h3>'
			text += '<ul class="list-group list-group-flush">'
			text += '	<li class="list-group-item">'
			text += '		<div class="row">'
			text += '			<div class="col">'
			text += '				<span>START DATE : </span> <span id="stDate">' + list[data]["stDate"] + '</span>'
			text += '			</div>'
			text += '			<div class="col">'
			text += '				<span>END DATE : </span> <span id="endDate">' + list[data]["endDate"] + '</span>'
			text += '			</div>'
			text += '		</div>'
			text += '	</li>'
			text += '</ul>'
			text += '<ul class="list-group list-group-flush">'
			text += '	<li class="list-group-item">'
			text += '		<div class="row">'
			text += '			<div class="col">'
			text += '				<span>SKILL : </span> <span id="endDate">' + list[data]["skill"] + '</span>'
			text += '			</div>'
			text += '		</div>'
			text += '	</li>'
			text += '</ul>'
			text += '<div class="card-body">'
			text += '	<h6>DETAIL</h6>'
			text += '	<blockquote class="blockquote">'
			text += '		<p class="mb-0">' + list[data]["content"].unescapeHtml() + '</p>'
			text += '	</blockquote>'
			text += '</div>'
			text += '</div>';
			
		$('#'+appendDivId).append(text);
	}
}
 
/**
 * 
 * @param appendDivId
 * @param list
 * @returns Intro에서 사용하는 desk list 뿌릴때씀.
 */
function viewDeskList(appendDivId, list){
	var appendDiv = $('#'+appendDivId);
	appendDiv.html("");
	
	var html = '<div class="card text-white bg-success mb-3 slide-item" style="width: 594px; height:250px; display:inline-block;">'
		html+='	<div class="card-header">New</div>'
		html+='<div class="card-body" style="text-align: center;">'
		html+='	<button type="button" id="btnAddIcon" class="btn" style=" cursor:pointer; outline:0;">'
		html+='		<i class="fas fa-plus fa-5x" style="color:white;"></i>'
		html+='	</button>'
		html+='	<br>'
		html+='	<h4 class="card-title">ADD</h4>'
		html+='</div>'
		html+='</div>';
	appendDiv.html(html);
	
	for(var data in list){
		
		var userList = list[data]["userList"];
		
		var userNameList = new Array();
		
		for(var i=0; i<userList.length; i++){
			userNameList.push(userList[i]["userName"]);
		}
		
		
		var cardId = "card"+data;
		
		var deskId = list[data]["deskId"]; 
		
		var text ='<div class="card text-white bg-info mb-3 slide-item cardList" style="width: 594px; height:250px; display:inline-block; cursor:pointer;">';
		text +='	<div class="card-header">참여중인 사람 : '+userNameList.toString()+' </div>'
		text +='	<div class="card-body">'
		text +='		<h4 class="card-title">'+ list[data]["deskName"] +'</h4>'
		text +='		<p class="card-text">'+ list[data]["comments"] +'</p>'
		text +='		<form id="frmCard'+data+' method="post" action="/desk/set">'
		text +='			<input type="text" name="deskId" style="display:none;" value="'+deskId+'">'
		text +='		</form>'
		text +='	</div>'
		text +='</div>';
		appendDiv.append(text);
	}
	
	var btnAddIcon = getEl("btnAddIcon");	// 팝업띄움
	var btnAddNote = getEl("btnAddNote"); // 서버로 데이터날리는 버튼
    
    var deskName = getEl("deskName"); // desk 이름
    var vaildMent = getEl("vaildMent"); // 합 불합 표시
    
    elementsEvents();
    
	//이벤트 모음
    function elementsEvents(){
    	// ADD POPUP EVENT
    	btnAddIcon.event("click",function(){
    		
			/* if("[[ ${session.SPRING_SECURITY_CONTEXT.authentication.authorities}]]".replaceAll("[", "").replaceAll("]","") != "ADMIN"){
				alert("잠시 게스트는 추가 못하도록 막아두고 있습니다!!");
				return false;
			} */
    		
			$bPopup.bPopup({
	            modalClose: false,
	            opacity: 0.6,
	            positionStyle: 'fixed', //'fixed' or 'absolute'
	            onOpen: function() {
	            	//열릴때 이벤트
	            }, 
	            onClose: function() { 
	            	//닫힐때 초기화
    	        	getEl("frmDeskInfo").reset();
	            }
	        });
    	});
    	
    	// ADD CLICK EVENT
    	btnAddNote.event("click", function(){
    		
    		//4글자 미만 팅김
    		if(deskName.value.length < 4){
    			return false;
    		}
    		
    		if(!confirm("생성 하시겠습니까?"))
    			return false;
    		
    		ajaxJsonParam("/intro/createDesk", $("#frmDeskInfo").serializeObject()).then(function(data){
    			if(!!data){
    				alert("생성 완료!");
    				$bPopup.bPopup().close();
    			} else {
    				alert("생성 실패?");
    			}
    			getDeskList().then(function(){
    				$bxSlider.reloadSlider();
    			});
    			
    		});
    	});
    	
    	//이름 키업이벤트로 4글자 미만이면 팅기게
    	deskName.event("keyup", function(){
	    	if(this.value.length < 4){
	    		
	    		if(!deskName.classList.contains("is-invalid")){
	    			deskName.classList.add("is-invalid");
	    		}
	    		
	    		vaildMent.classList.add("invalid-feedback");
	    		vaildMent.classList.remove("valid-feedback");
    			vaildMent.style.display = "block";
    			vaildMent.innerHTML = "최소 4글자 이상 입력해주세요";
	    		
	    	} else {
	    		if(deskName.classList.contains("is-invalid")){
	    			deskName.classList.remove("is-invalid");
	    			deskName.classList.add("is-valid");
	    		}
	    		
	    		vaildMent.classList.remove("invalid-feedback");
	    		vaildMent.classList.add("valid-feedback");
	    		vaildMent.innerHTML = "굿";
	    	}
	    });
    }
	
	var cardList = document.getElementsByClassName("cardList");
	
	for(var i=0; i<cardList.length; i++){
		cardList[i].event("click", function(){
			this.querySelector("form").submit();
			//location.href = "/desk/home?deskId="+this.querySelector("input").value;
		});
	}
//	
//	getEl(cardId).event("click", function(){
//		console.log(cardId);
//	});
}

function textAreaAutoSize(obj) {
	for(var i=0; i<obj.length; i++){
		obj[i].style.cssText = "height:" + (12+obj[i].scrollHeight)+"px !important";
	}
}
