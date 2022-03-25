import authHeader from "../../service/auth-header";

export function download(url, filename) {
  fetch(url, {
    headers: authHeader(),
  }).then(function (t) {
    return t.blob().then((b) => {
      var a = document.createElement("a");
      a.href = URL.createObjectURL(b);
      a.setAttribute("download", filename);
      a.click();
    });
  });
}
