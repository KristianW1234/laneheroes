export function getBareAuthHeaders(): HeadersInit {
  const token = localStorage.getItem("token") || "";
  return {
    "Authorization": `Bearer ${token}`
  };
}