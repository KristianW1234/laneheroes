"use client";


import { useState } from "react";
import { useRouter } from "next/navigation";
import { baseURL } from "@/utils/constants";

export default function LoginPage() {
  const [userId, setUserId] = useState("");
  const [userPassword, setUserPassword] = useState("");
  const [error, setError] = useState("");
  const router = useRouter();

  const handleLogin = async () => {
    
    try {
      const res = await fetch(`${baseURL}/api/auth/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          userName: userId,
          userPassword: userPassword,
        }),
      });

      const json = await res.json();

      console.log("RES IS: " + JSON.stringify(res));
      console.log("JSON IS: " + JSON.stringify(json));

      if (json.status === "Success") {
        // Save the session to localStorage

        console.log("OK!");
        localStorage.setItem("user", JSON.stringify(json.data.user));
        localStorage.setItem("token", json.data.token);

        // Redirect to main page
        router.push("/main");
      } else {
        console.log("Not OK!");
        setError("Invalid credentials.");
      }
    } catch (err) {
      console.error("Login error:", err);
      setError("Login failed.");
    }
  };

  return (
    <div className="flex items-center justify-center h-screen bg-gray-100">
      <div className="bg-white p-8 shadow rounded w-96">
        <h1 className="text-2xl font-bold mb-4">Login</h1>

        {error && <p className="text-red-500 mb-4">{error}</p>}

        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">User ID</label>
          <input
            type="text"
            value={userId}
            onChange={(e) => setUserId(e.target.value)}
            className="w-full border px-3 py-2 rounded"
          />
        </div>

        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">Password</label>
          <input
            type="password"
            value={userPassword}
            onChange={(e) => setUserPassword(e.target.value)}
            className="w-full border px-3 py-2 rounded"
          />
        </div>

        <button
          onClick={handleLogin}
          className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
        >
          Login
        </button>
      </div>
    </div>
  );
}