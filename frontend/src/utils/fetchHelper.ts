import { getFetchHeaders } from "@/utils/fetchHeaders";

export async function fetchData<T>(
  url: string,
  method: string = "GET",
  body?: unknown
): Promise<T> {
  const res = await fetch(url, {
    method,
    headers: getFetchHeaders(),
    body: body ? JSON.stringify(body) : undefined,
  });

  if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
  const json = await res.json();
  return json.data;
}