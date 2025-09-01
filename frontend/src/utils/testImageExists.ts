export async function testImageExists(
  url: string,
  onFail: () => void
): Promise<void> {
  try {
    const controller = new AbortController();
    const timeout = setTimeout(() => controller.abort(), 500);

    const res = await fetch(url, {
      method: "GET",
      signal: controller.signal,
      cache: "no-store"
    });

    clearTimeout(timeout);

    if (!res.ok) {
      onFail();
    }
  } catch {
    onFail(); // treat failure as fallback too
  }
}