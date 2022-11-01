import service from "@/request/index";
export let list = async()=>{    
   let data = await service.get('/practice/find')
   return data;
}