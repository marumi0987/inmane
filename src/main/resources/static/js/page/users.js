var Users = () => {
}

Users.beanErrorTextClear = (elName) => {
  if ($(elName).length) {
    $(elName).text("");
  }
}

Users.submitHandler = (formId, confirmMsg) => {
  const form = document.getElementById(formId);
  
  Users.beanErrorTextClear(".errorMsg");
  
  // form内でバリデーションエラーがある場合は終了
  if (!form.checkValidity()){
    // バリデーションエラーメッセージを表示
    form.reportValidity();
    return true;
  }
  // 確認ダイアログ
  App.dialogConfirm(confirmMsg, (val) => {
    if (val) form.submit();
    return val;
  });
}