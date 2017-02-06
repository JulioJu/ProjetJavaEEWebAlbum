var currentPath = window.location.href;
var contextPath =  window.location.href.split("/").slice(-2)
if (contextPath[0]==="pictures")
    document.getElementById("picturesFolder").style.display="block";
else
    document.getElementById("picturesFolder").style.display="none";
