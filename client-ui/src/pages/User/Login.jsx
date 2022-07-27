import React, { useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import {
  userSignIn,
  userProviderSignIn,
  reset,
} from "../../redux/features/UserSlice";
import { auth, provider, signInWithPopup } from "../../util/Firebase";
import { Link } from "react-router-dom";
import { toast } from "react-hot-toast";
import { useFormik } from "formik";
import * as Yup from "yup";
import { FcGoogle } from "react-icons/fc";
import "./Login.scss";

const Login = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { isLoading, isSuccess, error } = useSelector((state) => state.user);
  useEffect(() => {
    if (error) {
      toast.error(error);
    }
    if (location?.state?.from && isSuccess) {
      let path = location?.state.from;
      return navigate({ pathname: path, replace: true });
    }

    if (isSuccess) {
      toast.success("Authenticated !");
      navigate("/", { replace: true });
    }
    dispatch(reset());
  }, [isSuccess, error, location, navigate, dispatch]);
  const formik = useFormik({
    initialValues: {
      email: "",
      password: "",
    },
    onSubmit: async (values, { resetForm }) => {
      dispatch(reset());
      await dispatch(
        userSignIn({
          email: values.email,
          password: values.password,
        })
      );
      resetForm();
    },
    validationSchema: Yup.object({
      email: Yup.string()
        .email("please enter valid email")
        .required("Email is required"),
      password: Yup.string().required("Password is required"),
    }),
  });

  const signInWithGoogle = () => {
    signInWithPopup(auth, provider)
      .then(async (response) => {
        const {
          providerId,
          user: { email, displayName },
        } = response;
        await dispatch(
          userProviderSignIn({
            username: displayName,
            email,
            provider: providerId,
          })
        );
      })
      .catch((err) => {
        toast.error(error);
      });
  };
  return (
    <div className='login'>
      <div className='login__section'>
        <div className='login__section__form'>
          <form onSubmit={formik.handleSubmit}>
            <h3>Sign in to your account</h3>
            <div className='text-blk'>
              <label htmlFor='email'>Email</label>
              <input
                type='email'
                name='email'
                id='email'
                onBlur={formik.handleBlur}
                onChange={formik.handleChange}
                value={formik.values.email}
                autoComplete='new-username'
              />
              {formik.touched.email && (
                <span className='error'>{formik.errors.email}</span>
              )}
            </div>

            <div className='text-blk'>
              <label htmlFor='password'>Password</label>
              <input
                type='password'
                id='password'
                onBlur={formik.handleBlur}
                onChange={formik.handleChange}
                value={formik.values.password}
                autoComplete='new-password'
              />
              {formik.touched.password && (
                <span className='error'>{formik.errors.password}</span>
              )}
            </div>

            <button
              className='login__log-in'
              type='submit'
              disabled={isLoading}
            >
              Log In
            </button>
          </form>
          <button className='login__google-signIn' onClick={signInWithGoogle}>
            <FcGoogle />
            <span>Sign in with Google</span>
          </button>
          <p className='register-link'>
            Don't have an account? <Link to='/register'>Sign up</Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Login;
