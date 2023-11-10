// async : 함수 선언 시 사용, 비동기 처리를 위한 함수임을 명시
// bno : 현재 게시글 번호
// page : 댓글 페이지 번호
// size : 페이지당 댓글 개수
// goLast : 마지막 페이지 호출 여부 -> 페이징 처리 시 마지막 댓글 페이지부터 호출
// 페이징 처리된 댓글 목록 출력
async function getList({bno, page, size, goLast}) {

    // await : async 함수 내에서 비동기 호출하는 부분에 사용
    const result = await axios.get(`/replies/list/${bno}`, {params: {page, size}})  // GET 방식 호출

    if(goLast) {
        const total = result.data.total
        const lastPage = parseInt(Math.ceil(total/size))

        return getList({bno:bno, page:lastPage, size:size})
    }

    return result.data;
}

// 댓글 등록
async function addReply(replyObj) {
    
    const response = await axios.post(`/replies/`, replyObj) // POST 방식 호출
    
    return response.data
}

// 특정 댓글 상세 조회
async function getReply(rno) {

    const response = await axios.get(`/replies/${rno}`)

    return response.data
}

// 댓글 수정
async function modifyReply(replyObj) {

    const response = await axios.put(`/replies/${replyObj.rno}`, replyObj)

    return response.data
}

// 댓글 삭제
async function removeReply(rno) {

    const response = await axios.delete(`/replies/${rno}`)

    return response.data
}