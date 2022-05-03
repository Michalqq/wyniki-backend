import authHeader from "../../service/auth-header";

export function download(url, filename, handleResponse) {
  fetch(url, {
    headers: authHeader(),
  }).then(function (t) {
    return t.blob().then((b) => {
      var a = document.createElement("a");
      a.href = URL.createObjectURL(b);
      a.setAttribute("download", filename);
      a.click();
      if (handleResponse) handleResponse();
    });
  });
}

export function openFile(file, filename) {
  //window.open(`data:application/${filename.split(".").pop()};base64, ` + file);
  var bytes = _base64ToArrayBuffer(file);
  var link = new Blob([bytes], {
    type: "application/" + filename.split(".").pop(),
  });
  var a = document.createElement("a");
  a.href = URL.createObjectURL(link);
  a.setAttribute("download", filename);
  a.click();
}

//Base64 conversion
function _base64ToArrayBuffer(base64) {
  var binary_string = window.atob(base64);
  var len = binary_string.length;
  var bytes = new Uint8Array(len);
  for (var i = 0; i < len; i++) {
    bytes[i] = binary_string.charCodeAt(i);
  }
  return bytes.buffer;
}
