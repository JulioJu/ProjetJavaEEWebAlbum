// http://www.w3schools.com/howto/howto_css_modal_images.asp

function modalImage(modalId, thumbId, modalImgId, captionId, closeId) {
    // Get the modal
    var modal = document.getElementById(modalId);

    // Get the image and insert it inside the modal - use its "alt" text as a caption
    var img = document.getElementById(thumbId);
    var modalImg = document.getElementById(modalImgId);
    var captionText = document.getElementById(captionId);
    img.onclick = function(){
        modal.style.display = "block";
        modalImg.src = this.src;
        captionText.innerHTML = this.alt;
    }

    // Get the <span> element that closes the modal
    var span = document.getElementById(closeId);

    // When the user clicks on <span> (x), close the modal
    span.onclick = function() {
    modal.style.display = "none";
    }

    modal.onclick = function() {
    modal.style.display = "none";
    }
}

// vim: sw=4 ts=4 noet:
