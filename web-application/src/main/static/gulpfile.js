/*
 * Copyright 2016 Microprofile.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
var webapp = 'ROOT';
var resources = 'static-resources';
var target = '../../../target';
var gulp = require('gulp');
var ts = require('gulp-typescript');
var tslint = require('gulp-tslint');
var sourcemaps = require('gulp-sourcemaps');
var uglify = require('gulp-uglify');
var concat = require('gulp-concat');
var del = require('del');
var gulpsync = require('gulp-sync')(gulp);
var jade = require('gulp-pug');
var sass = require('gulp-sass');
var es = require('event-stream');
var autoprefixer = require('gulp-autoprefixer');
var KarmaServer = require('karma').Server;
var angularTemplateCache = require('gulp-angular-templatecache');

/**
 * Run all css & image tasks
 */
gulp.task('css', gulpsync.sync(['images', 'css-third-party']));

/**
 * Copy images from assets to
 */
gulp.task('images', function () {
    return gulp.src('./app/assets/**/*.{gif,jpg,png,svg}')
        .pipe(gulp.dest(target + '/' + resources + '/assets'));
});

gulp.task('css-third-party', function () {
    return gulp.src([
        './node_modules/bootstrap/dist/css/bootstrap.min.css'
    ]).pipe(gulp.dest(target + '/' + resources + '/assets/css/'));
});

gulp.task('js', gulpsync.sync(['compile-ts', 'js-third-party', 'js-bundles']));

gulp.task('lint-ts', function () {
    return gulp.src('./app/**/*.ts')
        .pipe(tslint())
        .pipe(tslint.report('prose'));
});

gulp.task('compile-ts', function () {
    return gulp.src('./app/**/*.ts')
        .pipe(sourcemaps.init())
        .pipe(ts({
            'noImplicitAny': false,
            'experimentalDecorators': true,
            'target': 'es5',
            'allowJs': false,
            'suppressImplicitAnyIndexErrors': true,
            'emitDecoratorMetadata': true,
            'forceConsistentCasingInFileNames': true,
            'listFiles': true,
            'sourceMap': true
        }))
        .pipe(uglify({
            mangle: false // otherwhise the sourcemap/debugger does not work properly.
        }))
        .pipe(sourcemaps.write({includeContent: false}))
        .pipe(gulp.dest(target + '/' + resources + '/app/'));
});

gulp.task('js-third-party', function () {

    var scripts = [
        './systemjs.config.js',
        './node_modules/core-js/client/shim.min.js',
        './node_modules/zone.js/dist/zone.js',
        './node_modules/reflect-metadata/Reflect.js',
        './node_modules/systemjs/dist/system.src.js',
        // angular2
        './node_modules/@angular/core/bundles/core.umd.js',
        './node_modules/@angular/common/bundles/common.umd.js',
        './node_modules/@angular/compiler/bundles/compiler.umd.js',
        './node_modules/@angular/platform-browser/bundles/platform-browser.umd.js',
        './node_modules/@angular/platform-browser-dynamic/bundles/platform-browser-dynamic.umd.js',
        './node_modules/@angular/http/bundles/http.umd.js',
        './node_modules/@angular/router/bundles/router.umd.js',
        './node_modules/@angular/forms/bundles/forms.umd.js'
    ];

    var tasks = [];
    var s;
    for (s in scripts) {
        tasks.push(buildTask(scripts[s]));
    }

    return es.concat(tasks);
});

function buildTask(/*String*/path) {
    console.log("" + path);
    return gulp.src(path).pipe(gulp.dest(target + '/' + resources + '/assets/js/' + path.substr(0, path.lastIndexOf('/')) + '/'));
}

gulp.task('js-bundles', function () {

    var rxjs = gulp.src([
        './node_modules/rxjs/**/*.js'
    ], {base: './node_modules/rxjs/'}).pipe(gulp.dest(target + '/' + resources + '/assets/js/node_modules/rxjs'));

    var wapi = gulp.src([
        './node_modules/angular2-in-memory-web-api/**/*.js'
    ], {base: './node_modules/angular2-in-memory-web-api/'}).pipe(gulp.dest(target + '/' + resources + '/assets/js/node_modules/angular2-in-memory-web-api'));
    return es.concat(rxjs, wapi);
});

gulp.task('test', function (done) {
    new KarmaServer({
        configFile: __dirname + '/karma.conf.js'
    }, done).start();
});

gulp.task('copy-all', function () {
    return gulp.src([
        target + '/' + resources + '/**/*.css',
        target + '/' + resources + '/**/*.js',
        target + '/' + resources + '/assets'
    ]).pipe(gulp.dest(target + '/apache-tomee/webapps/' + webapp + '/'));
});

gulp.task('clean', function (callback) {
    return del([
        target + '/' + resources + '/',
        target + '/apache-tomee/webapps/' + webapp + '/app/',
        target + '/apache-tomee/webapps/' + webapp + '/components/'
    ], {
        force: true
    }, callback);
});

gulp.task('build', gulpsync.sync(['clean', 'js', 'css', 'copy-all']));
//gulp.task('build-with-tests', gulpsync.sync(['build', 'test']));

gulp.task('default', gulpsync.sync(['build']), function () {
    gulp.watch(
        ['./app/**/*', '../../test/**/*.js'],
        gulpsync.sync(['build'])
    );
});
