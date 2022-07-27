import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import http from "../../api/axios";
const initialState = {
  userDetails: {
    username: null,
    token: null,
  },
  isSuccess: false,
  isLoading: false,
  error: null,
};

export const userSignIn = createAsyncThunk(
  "user/login",
  async (userPayload, { rejectWithValue }) => {
    try {
      const { data } = await http.post("/auth/user/signin", userPayload);
      return data;
    } catch (error) {
      if (error.response) {
        return rejectWithValue(error.response.data.message);
      }
      return rejectWithValue(error.message);
    }
  }
);

export const userProviderSignIn = createAsyncThunk(
  "user/provider",
  async (user, { rejectWithValue }) => {
    try {
      const { data } = await http.post("/auth/provider/signin", user);
      return data;
    } catch (error) {
      if (error.response) {
        return rejectWithValue(error.response.data.message);
      }
      return rejectWithValue(error.message);
    }
  }
);
export const userSignUp = createAsyncThunk(
  "user/register",
  async (userPayload, { rejectWithValue }) => {
    try {
      const { data } = await http.post("/auth/user/signup", userPayload);
      return data;
    } catch (error) {
      if (error.response) return rejectWithValue(error.response.data.message);
      return rejectWithValue(error.message);
    }
  }
);
const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    logout: (state) => {
      return {
        ...state,
        userDetails: {
          username: null,
          token: null,
        },
        isSuccess: false,
        isLoading: false,
        error: null,
      };
    },
    reset: (state) => {
      state.isSuccess = false;
      state.isLoading = false;
      state.error = null;
    },
  },
  extraReducers: {
    [userSignIn.fulfilled]: (state, action) => {
      const { username, accessToken } = action.payload;
      return {
        ...state,
        userDetails: {
          username,
          token: accessToken,
        },
        isSuccess: true,
        isLoading: false,
        error: null,
      };
    },
    [userSignIn.pending]: (state) => {
      state.isLoading = true;
    },
    [userSignIn.rejected]: (state, action) => {
      state.error = action.payload ? action.payload : "something went wrong";
      state.isSuccess = false;
      state.isLoading = false;
    },
    [userProviderSignIn.fulfilled]: (state, action) => {
      const { username, accessToken } = action.payload;
      return {
        ...state,
        userDetails: {
          username,
          token: accessToken,
        },
        isSuccess: true,
        isLoading: false,
        error: null,
      };
    },
    [userProviderSignIn.pending]: (state) => {
      state.isLoading = true;
    },
    [userProviderSignIn.rejected]: (state, action) => {
      state.error = action.payload ? action.payload : "something went wrong";
      state.isSuccess = false;
      state.isLoading = false;
    },
    [userSignUp.fulfilled]: (state, action) => {
      state.isSuccess = true;
      state.isLoading = false;
      state.error = null;
    },

    [userSignUp.pending]: (state) => {
      state.isLoading = true;
    },

    [userSignUp.rejected]: (state, action) => {
      state.error = action.payload ? action.payload : "something went wrong";
      state.isSuccess = false;
      state.isLoading = false;
    },
  },
});
export const { logout, reset } = userSlice.actions;
export default userSlice.reducer;
