export async function testImageExistsForPreview(
  url: string,
  onSuccess: () => void,
  onFail: () => void
) {
  try {
    const controller = new AbortController();
    const timeout = setTimeout(() => controller.abort(), 500);

    const res = await fetch(url, {
      method: "GET",
      signal: controller.signal,
      cache: "no-store"
    });

    clearTimeout(timeout);

    if (res.ok) {
      onSuccess(); // ✅ File exists
    } else {
      onFail(); // ❌ 404 or error
    }
  } catch (err) {
    console.error("Error checking image:", err);
    onFail();
  }
}
