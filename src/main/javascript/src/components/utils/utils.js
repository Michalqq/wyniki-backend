export const formatDate = (isoDate) => {
  let date = new Date(isoDate);
  let year = date.getFullYear();
  let month = date.getMonth() + 1;
  let dt = date.getDate();

  if (dt < 10) {
    dt = "0" + dt;
  }
  if (month < 10) {
    month = "0" + month;
  }
  return dt + "-" + month + "-" + year;
};

export const dotDateFormat = (isoDate) => {
  let date = new Date(isoDate);
  let year = date.getFullYear();
  let month = date.getMonth() + 1;
  let dt = date.getDate();

  if (dt < 10) {
    dt = "0" + dt;
  }
  if (month < 10) {
    month = "0" + month;
  }
  return dt + "." + month + "." + year;
};

export const isObjectEmpty = (obj) => {
  return obj && Object.keys(obj).length === 0 && obj.constructor === Object;
};

export function getCurrentDate(separator = ".") {
  let newDate = new Date();
  let date = newDate.getDate();
  let month = newDate.getMonth() + 1;
  let year = newDate.getFullYear();

  return `${date}${separator}${
    month < 10 ? `0${month}` : `${month}`
  }${separator}${year}`;
}

export const dashDateFormat = (isoDate) => {
  let date = new Date(isoDate);
  let year = date.getFullYear();
  let month = date.getMonth() + 1;
  let dt = date.getDate();

  if (dt < 10) {
    dt = "0" + dt;
  }
  if (month < 10) {
    month = "0" + month;
  }
  return year + "-" + month + "-" + dt;
};

export const dateToIsoDateFormat = (isoDate) => {
  let date = new Date(isoDate);
  let year = date.getFullYear();
  let month = date.getMonth() + 1;
  let dt = date.getDate();

  if (dt < 10) {
    dt = "0" + dt;
  }
  if (month < 10) {
    month = "0" + month;
  }
  return year + "-" + month + "-" + dt + "T00:00:00Z";
};

export const isoEndDate = (isoDate) => {
  let date = new Date(isoDate);
  let year = date.getFullYear();
  let month = date.getMonth() + 1;
  let dt = date.getDate();

  if (dt < 10) {
    dt = "0" + dt;
  }
  if (month < 10) {
    month = "0" + month;
  }
  return year + "-" + month + "-" + dt + "T23:59:59Z";
};

export const dashDateFormatMinusOneDay = (isoDate) => {
  let date = new Date(isoDate);
  let year = date.getFullYear();
  let month = date.getMonth() + 1;
  let dt = date.getDate() - 1;

  if (dt < 10) {
    dt = "0" + dt;
  }
  if (month < 10) {
    month = "0" + month;
  }
  return year + "-" + month + "-" + dt;
};

export const checkDate = (filename) => {
  const fileNameLength = filename.length;

  let startSign = fileNameLength - 14;
  let endSign = fileNameLength - 4;

  let fileDate = filename.substring(startSign, endSign);
  fileDate = fileDate + "T23:59:50";

  var actualDate = new Date();
  var checkingDate = new Date(fileDate);
  return actualDate.getTime() <= checkingDate.getTime();
};

export const closeOnBack = (closeHandler) => {
  window.history.pushState(null, "", window.location.href);
  window.onpopstate = () => {
    window.history.pushState(null, "", window.location.href);
    window.onpopstate = undefined;
    window.history.back();
    closeHandler();
  };
};

export const calcTimeTo = (tempScores) => {
  if (tempScores.length === 0) return [];
  tempScores = JSON.parse(JSON.stringify(tempScores));

  let score;
  let firstScore = tempScores[0];
  for (const element of tempScores) {
    if (score) {
      let timeTo = element.totalTimeWithPenalty - score.totalTimeWithPenalty;
      let timeToFirst =
        element.totalTimeWithPenalty - firstScore.totalTimeWithPenalty;
      element.timeTo = "+" + timeToString(timeTo);
      element.timeToFirst = "+" + timeToString(timeToFirst);
    } else {
      element.timeTo = "";
      element.timeToFirst = "";
    }
    score = element;
  }
  return tempScores;
};

export const timeToString = (time) => {
  let seconds = Math.floor(time / 1000);
  let minutes = Math.floor(seconds / 60);

  seconds = seconds % 60;
  minutes = minutes % 60;

  let milis = Math.floor((time - minutes * 60000 - seconds * 1000) / 10);
  return (
    (minutes > 0 ? `${minutes}:` : "") +
    `${padTo2Digits(seconds)}.${padTo2Digits(milis)}`
  );
};

function padTo2Digits(num) {
  return num.toString().padStart(2, "0");
}
