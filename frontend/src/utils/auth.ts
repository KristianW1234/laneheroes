export function isTokenExpired(token: string | null): boolean {
  if (!token) return true;

  try {
    const payloadBase64 = token.split('.')[1];
    const decodedPayload = JSON.parse(atob(payloadBase64));
    const exp = decodedPayload.exp * 1000; // seconds → ms
    return Date.now() > exp;
  } catch (e) {
    console.error("Invalid token format", e);
    return true;
  }
}