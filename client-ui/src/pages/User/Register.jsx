import React, { useEffect } from "react";
import { useFormik } from "formik";
import { Link, useNavigate } from "react-router-dom";
import { toast } from "react-hot-toast";
import { useDispatch, useSelector } from "react-redux";
import { userSignUp, reset } from "../../redux/features/UserSlice";
import * as Yup from "yup";
import "./Register.scss";

const Register = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { isLoading, isSuccess, error } = useSelector((state) => state.user);

  useEffect(() => {
    if (error) {
      toast.error(error);
    }
    if (isSuccess) {
      toast.success("Registered Successfully !");
      navigate("/login", { replace: true });
    }
    dispatch(reset());
  }, [isSuccess, error, navigate, dispatch]);
  const formik = useFormik({
    initialValues: {
      username: "",
      email: "",
      password: "",
      confirmPassword: "",
    },

    onSubmit: async (values, { resetForm }) => {
      dispatch(reset());
      await dispatch(
        userSignUp({
          username: values.username,
          email: values.email,
          password: values.password,
        })
      );
      resetForm();
    },

    validationSchema: Yup.object({
      username: Yup.string().required("username is required"),
      email: Yup.string()
        .email("please enter valid email")
        .required("Email is required"),
      password: Yup.string()
        .min(8, "Password atleast more than 8 character length")
        .required("Password is required"),
      confirmPassword: Yup.string().when("password", {
        is: (val) => (val && val.length > 0 ? true : false),
        then: Yup.string().oneOf(
          [Yup.ref("password")],
          "Both password need to be the same"
        ),
      }),
    }),
  });

  const { handleSubmit, handleBlur, handleChange, values, touched, errors } =
    formik;
  return (
    <div className='register'>
      <div className='register__section'>
        <form className='register__section__form' onSubmit={handleSubmit}>
          <h3>Create your account</h3>
          <div className='text-blk'>
            <label htmlFor='username'>Username</label>
            <input
              type='text'
              name='username'
              id='username'
              onChange={handleChange}
              onBlur={handleBlur}
              value={values.username}
            />
            {touched.username && (
              <span className='error'>{errors.username}</span>
            )}
          </div>

          <div className='text-blk'>
            <label htmlFor='email'>Email</label>
            <input
              type='email'
              name='email'
              id='email'
              onBlur={handleBlur}
              onChange={handleChange}
              value={values.email}
            />
            {touched.email && <span className='error'>{errors.email}</span>}
          </div>

          <div className='text-blk'>
            <label htmlFor='password'>Password</label>
            <input
              type='password'
              name='password'
              id='password'
              onBlur={handleBlur}
              onChange={handleChange}
              value={values.password}
              autocomplete='new-password'
            />
            {touched.password && (
              <span className='error'>{errors.password}</span>
            )}
          </div>

          <div className='text-blk'>
            <label htmlFor='confirmPassword'>ConfirmPassword</label>
            <input
              type='password'
              name='confirmPassword'
              id='confirmPassword'
              onBlur={handleBlur}
              onChange={handleChange}
              value={values.confirmPassword}
              autocomplete='new-password'
            />
            {touched.confirmPassword && (
              <span className='error'>{errors.confirmPassword}</span>
            )}
          </div>
          <div>
            <button type='submit' disabled={isLoading}>
              Register
            </button>
          </div>
          <p className='login-link'>
            Have an account?
            <Link className='ref' to='/login'>
              Sign in
            </Link>
          </p>
        </form>
      </div>
    </div>
  );
};

export default Register;
