import  axios  from "axios";
const { VITE_API_BASE_URL } = import.meta.env

const noticeAxios = () => {
    return axios.create({
        baseURL: VITE_API_BASE_URL,
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        }
    })
}

export {noticeAxios}

