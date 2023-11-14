// 서버에 첨부파일 업로드
async function uploadToServer(formObj) {

    console.log("서버에 파일 업로드 처리")
    console.log(formObj)

    const response = await axios({
        method: 'post',
        url: '/upload',
        data: formObj,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })

    return response.data
}

// 서버에서 특정 첨부파일 삭제
async function removeFileToserver(uuid, fileName) {

    const response = await axios.delete(`/remove/${uuid}_${fileName}`)

    return response.data
}