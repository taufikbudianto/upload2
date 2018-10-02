/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function saveAsImage() {
    $("#img-out").children().remove();
    html2canvas($("#idMap"), {
        useCORS: true,
        onrendered: function (canvas) {
            theCanvas = canvas;
            document.body.appendChild(canvas);
            $("#img-out").append(canvas);
        }
    });
}
