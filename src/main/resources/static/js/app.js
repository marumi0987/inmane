var App = () => {
}

App.logout = () => {
  let message = "ログアウトしますがよろしいですか？";
  let url = "/inmane/security/logout";

  App.dialogConfirm(message, (val) => {
    if (val) {
      location.href = url;
      return true;
    }
    return false;
  });
}
App.msgModal = (msg, url) => {
  showModalDialog(msg, url);
}
App.onclickUserName = () => {
  let chkUserName = document.getElementById("chkUserName");
  let btnLogout = document.getElementById("btnLogout");
  let btnSettings = document.getElementById("btnSettings");
  btnLogout.disabled = !chkUserName.checked;
  btnSettings.disabled = !chkUserName.checked;

}

/* -------------------------------------------------------------------------- */
/* 画面読込・画面遷移関数 */
/* -------------------------------------------------------------------------- */
App.contextPath = () => {
  return $("body").data("contextPath");
}
App.apiPath = (path) => {
  return App.contextPath() + $("body").data("selectedOfficeId") + "/" + path;
}
App.complete = (message, url)  => {
  if (url) {
    $("#formCompleteMessage").val(message);
    $("#formCompleteUrl").val(url);
    App.submit("#formComplete");
  } else if (message) {
    App.dialogComplete(message);
  }
}
App.doOnLoad = () => {
}

$(() => {

  // Flashスコープの例外メッセージ表示
  if ($("#appCompleteMessageFailure").val()) {
    App.dialogAlert($("#appCompleteMessageFailure").val())
  }
  // Flashスコープの警告メッセージ表示
  else if ($("#appCompleteMessageWarning").val()) {
    App.dialogAlert($("#appCompleteMessageWarning").val());
  }
  // Flashスコープの完了メッセージ表示
  else if ($("#appCompleteMessageSuccess").val()) {
    App.dialogSuccess($("#appCompleteMessageSuccess").val());
  }
});
/* -------------------------------------------------------------------------- */
/* ダイアログ */
/* -------------------------------------------------------------------------- */
App.dialogSuccess = (message, callback) =>{
  swal({
    title : "",
    text : message,
    type : "success",
    buttonsStyling : false,
    confirmButtonText : 'OK',
    confirmButtonClass : 'btn btn-md g-width-160--md g-mx-2 u-btn-primary',
  }).then(() => {
    if (callback) {
      callback();
    }
  });
}
App.dialogInfo = (message, callback) =>{
  swal({
    title : "",
    text : message,
    type : "info",
    buttonsStyling : false,
    confirmButtonText : 'はい',
    confirmButtonClass : 'btn btn-md g-width-160--md g-mx-2 u-btn-blue',
  }).then(() => {
    if (callback) {
      callback();
    }
  });
}
App.dialogAlert = (message, callback) =>{
  swal({
    title : "",
    text : message,
    type : "warning",
    buttonsStyling : false,
    confirmButtonText : 'OK',
    confirmButtonClass : 'btn btn-md g-width-160--md g-mx-2 u-btn-orange',
  }).then((state) => {
    setTimeout(() => {
      if (callback) {
        callback();
      }
    }, 100);
  });
}
App.dialogConfirm = (message, callback) =>{
  swal(
      {
        title : "",
        text : message,
        type : "info",
        showCancelButton : true,
        buttonsStyling : true,
        confirmButtonText : 'はい',
        cancelButtonText : 'キャンセル',
        confirmButtonClass : 'btn btn-md g-width-160--md g-mx-2 u-btn-blue',
        cancelButtonClass : 'btn btn-md g-width-160--md g-mx-2 u-btn-outline-gray-dark-v6',
      }).then((state) => {
    if (callback) {
      callback(state.value);
    }
  });
}
App.dialogConfirmWarning = (message, callback) =>{
  swal(
      {
        title : "",
        text : message,
        type : "warning",
        showCancelButton : true,
        buttonsStyling : false,
        confirmButtonText : 'はい',
        cancelButtonText : 'いいえ',
        confirmButtonClass : 'btn btn-md g-width-160--md g-mx-2 u-btn-orange',
        cancelButtonClass : 'btn btn-md g-width-160--md g-mx-2 u-btn-outline-gray-dark-v6',
      }).then((state) => {
    if (callback) {
      callback(state.value);
    }
  });
}

/* -------------------------------------------------------------------------- */
/* エラー制御 */
/* -------------------------------------------------------------------------- */
App.btnEnabled = (scope, btnSelector) => {

  App.btnEnabledCheck(scope, btnSelector);

  $(document)
      .on(
          "input change",
          $(scope)
              .find(
                  "input.required, select.required, textarea.required"),
          () => {
            App.btnEnabledCheck(scope, btnSelector)
          });
}
App.btnEnabledCheck = (scope, btnSelector) => {

  var empty = false;

  $(scope).find("input.required,select.required, textarea.required").each(
      () => {
        if ($(this).val() == "") {
          $(btnSelector).prop("disabled", true);
          empty = true;
          return false;
        }
      });
  if (!empty) {
    $(btnSelector).prop("disabled", false);
  }
}

App.errorReset = (scope) => {

  if (!scope) {
    scope = $("body");
  }

  $(scope).find(".valid-error").removeClass("valid-error");
  $(scope).find(".valid-error-msg").html("");
}
App.errorMsg = (scope, name, message) => {

  $input = $(scope).find("[name='" + name + "'][type!='hidden']");

  if ($input.size() > 0) {

    $input.addClass("valid-error");

    if ($(scope).find("." + name + "-msg").size() == 0) {
      $input.after("<span class='" + name
          + "-msg valid-error-msg'></span>");
    }
  }

  $(scope).find("." + name + "-msg").hide().addClass("valid-error-msg").html(
      "<span>" + message + "</span>").fadeIn();
}


