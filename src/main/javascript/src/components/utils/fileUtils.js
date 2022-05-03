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
  window.open(`data:application/${filename.split(".").pop()};base64, ` + file);
  // var file = new Blob([file]);
  // var urlFile = URL.createObjectURL(file);
  // window.open(urlFile);
  // var a = document.createElement("a");
  //a.href = URL.createObjectURL(file);
  //a.setAttribute("download", filename);
  //a.click();
}
