/* -------------------------------------------------------------------------- */
/* 掲示板 */
/* -------------------------------------------------------------------------- */

$(function() {
  var outer = $("<div>").css({
    visibility: "hidden",
    width: 100,
    overflow: "scroll",
  }).appendTo("body"), width = $("<div>").css({
    width: "100%"
  }).appendTo(outer).outerWidth(), result = 0;

  outer.remove();
  result = 100 - width;
  $(".table-fixed thead").css({
    paddingRight: result
  });
});

// 文字数カウント
function ShowLength(str) {

  var max = str.length;
  var color = "";

  if (max < 80) {
    color = "green";
  } else if (max < 90) {
    color = "yellow";
  } else if (max < 100) {
    color = "orange";
  } else {
    color = "red";
  }

  // 出力
  document.getElementById("inputlength").innerHTML = str.length + "文字";
  document.getElementById("inputlength").style.color = color;
}

$('[data-toggle="tooltip"]').tooltip();

// モーダル表示用
$('#myModal').on('shown.bs.modal', function() {
  // $('#myInput').trigger('focus');
})

// テキストボックスに値を設定
$(function() {
  $(".editButton").click(function() {

    var modal = $(this);
    let id = $('#id'  + $(this).val() ).html();
    let name = $('#beforeEditName' + $(this).val()).html();
    console.log("呼ばれました！！！");
    let message = $('#beforeEditMessage' + $(this).val()).html();

    $("#editId").val(id);
    $("#editName").val(name);
    $("#editMessage").val(message);

    // テキストボックスのデータを取得します ☆テキストだけなら出力できる
    // var getIdData = $("#replyId").val();
    // var getNameData = $("#beforeEditName").val();
    // var getMessageData = $("#beforeEditMessage").val();

    // これは出力できる▼
    // var getNameData ="テストです";
    // var getMessageData ="テストです";

    // テキストボックスへ値を設定します
    // $("#editId").val( getIdData );
    // $("#editName").val( getNameData );
    // $("#editMessage").val( getMessageData );
  });
});
